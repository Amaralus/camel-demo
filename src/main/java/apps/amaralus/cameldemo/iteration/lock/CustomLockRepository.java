package apps.amaralus.cameldemo.iteration.lock;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.integration.jdbc.lock.DefaultLockRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.Date;

/**
 * Кастомный лок репозиторий с единственной целью заменить updateQuery.
 * Разработчики Spring добавили возможность кастомизировать запросы
 * в одной из последних версий Spring Integration (spring 6, boot 3),
 * потому тут приходится выкручиваться.
 */
public class CustomLockRepository extends DefaultLockRepository {

    private final JdbcTemplate template;
    private final String id;
    private String region = "DEFAULT";
    private int ttl = DEFAULT_TTL;
    private String prefix;

    private String updateQuery =
            "UPDATE %sLOCK SET CLIENT_ID=?, CREATED_DATE=? WHERE REGION=? AND LOCK_KEY=? " +
            "AND CLIENT_ID=? AND CREATED_DATE<?";

    private String insertQuery = "INSERT INTO %sLOCK (REGION, LOCK_KEY, CLIENT_ID, CREATED_DATE) VALUES (?, ?, ?, ?)";

    public CustomLockRepository(DataSource dataSource, String id) {
        super(dataSource, id);
        this.template = new JdbcTemplate(dataSource);
        this.id = id;
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        this.updateQuery = String.format(this.updateQuery, this.prefix);
        this.insertQuery = String.format(this.insertQuery, this.prefix);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public boolean acquire(String lock) {
        if (this.template.update(this.updateQuery, this.id, new Date(), this.region, lock, this.id,
                new Date(System.currentTimeMillis() - this.ttl)) > 0) {
            return true;
        }
        try {
            return this.template.update(this.insertQuery, this.region, lock, this.id, new Date()) > 0;
        } catch (DuplicateKeyException e) {
            return false;
        }
    }

    @Override
    public void setRegion(String region) {
        super.setRegion(region);
        this.region = region;
    }

    @Override
    public void setTimeToLive(int timeToLive) {
        super.setTimeToLive(timeToLive);
        this.ttl = timeToLive;
    }

    @Override
    public void setPrefix(String prefix) {
        super.setPrefix(prefix);
        this.prefix = prefix;
    }
}

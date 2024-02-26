package apps.amaralus.cameldemo.iteration.lock;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.jdbc.lock.DefaultLockRepository;
import org.springframework.integration.jdbc.lock.LockRepository;

import javax.sql.DataSource;
import java.time.Duration;

@Configuration
public class LockConfig {

    @Bean
    public LockRepository customLockRepository(ApplicationContext applicationContext,
                                               DataSource dataSource,
                                               @Value("${app.lockDurationMinutes}") int lockDurationMinutes,
                                               @Value("${app.database.schema-name}") String schemaName) {
        var customLockRepository = new CustomLockRepository(dataSource, applicationContext.getId());
        customLockRepository.setPrefix(schemaName + "." + DefaultLockRepository.DEFAULT_TABLE_PREFIX);
        customLockRepository.setTimeToLive((int) Duration.ofMinutes(lockDurationMinutes).toMillis());
        return customLockRepository;
    }
}

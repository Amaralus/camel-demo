package apps.amaralus.cameldemo.iteration.lock;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.jdbc.lock.DefaultLockRepository;

import javax.sql.DataSource;
import java.time.Duration;

@Configuration
public class LockConfig {

    @Bean
    public DefaultLockRepository defaultLockRepository(DataSource dataSource,
                                                       @Value("${app.lockDurationMinutes}") int lockDurationMinutes,
                                                       @Value("${spring.application.name}") String applicationName) {
        var defaultLockRepository = new DefaultLockRepository(dataSource, applicationName);
        defaultLockRepository.setPrefix("demo.int_");
        defaultLockRepository.setTimeToLive((int) Duration.ofMinutes(lockDurationMinutes).toMillis());
//        defaultLockRepository.setUpdateQuery(
//                "UPDATE %sLOCK\n" +
//                "SET CLIENT_ID=?, CREATED_DATE=?\n" +
//                "WHERE REGION=? AND LOCK_KEY=? AND CLIENT_ID=? AND CREATED_DATE<?\n");
        return defaultLockRepository;
    }
}

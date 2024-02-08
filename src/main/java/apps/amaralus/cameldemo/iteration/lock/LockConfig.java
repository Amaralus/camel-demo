package apps.amaralus.cameldemo.iteration.lock;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.jdbc.lock.DefaultLockRepository;

import javax.sql.DataSource;

@Configuration
public class LockConfig {

    @Bean
    public DefaultLockRepository defaultLockRepository(DataSource dataSource,
                                                       @Value("${app.lockExpireMillis}") int lockExpireMillis,
                                                       @Value("${spring.application.name}") String applicationName) {
        var defaultLockRepository = new DefaultLockRepository(dataSource, applicationName);
        defaultLockRepository.setPrefix("demo.int_");
        defaultLockRepository.setTimeToLive(lockExpireMillis);
        defaultLockRepository.setUpdateQuery("""
                UPDATE %sLOCK
                SET CLIENT_ID=?, CREATED_DATE=?
                WHERE REGION=? AND LOCK_KEY=? AND CLIENT_ID=? AND CREATED_DATE<?
                """);
        return defaultLockRepository;
    }
}

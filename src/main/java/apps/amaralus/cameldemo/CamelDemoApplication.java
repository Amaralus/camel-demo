package apps.amaralus.cameldemo;

import org.apache.camel.spi.Policy;
import org.apache.camel.spring.spi.SpringTransactionPolicy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootApplication
public class CamelDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(CamelDemoApplication.class, args);
    }

    @Bean
    public Policy serializablePolicy(PlatformTransactionManager transactionManager) {
        var transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_SERIALIZABLE);
        return new SpringTransactionPolicy(transactionTemplate);
    }

}

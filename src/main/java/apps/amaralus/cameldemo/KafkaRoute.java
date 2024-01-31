package apps.amaralus.cameldemo;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class KafkaRoute extends RouteBuilder {
    @Override
    public void configure() {
        from("kafka:{{app.kafka.topic-in}}")
                .to("kafka:{{app.kafka.topic-out}}");
    }
}

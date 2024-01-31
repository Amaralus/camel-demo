package apps.amaralus.cameldemo;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class KafkaRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("kafka:test-in")
                .to("kafka:test-out");
    }
}

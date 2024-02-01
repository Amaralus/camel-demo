package apps.amaralus.cameldemo;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.stereotype.Component;

@Component
public class KafkaRoute extends RouteBuilder {
    @Override
    public void configure() {
        from("kafka:{{app.kafka.topic-in}}")
                .unmarshal(new JacksonDataFormat(SomeData.class))
                .to("bean-validator://x")
                .bean(MyService.class, "processData")
                .marshal(new JacksonDataFormat(SomeData.class))
                .to("kafka:{{app.kafka.topic-out}}");
    }
}

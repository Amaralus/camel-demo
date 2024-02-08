package apps.amaralus.cameldemo.iteration;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class KafkaTriggerRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("kafka:{{app.kafka.topic-trigger}}")
            .to("direct:aggregation-completion");
    }
}

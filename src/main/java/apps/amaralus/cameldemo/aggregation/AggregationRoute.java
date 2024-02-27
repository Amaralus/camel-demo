package apps.amaralus.cameldemo.aggregation;

import apps.amaralus.cameldemo.aggregation.model.InputMessage;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
public class AggregationRoute extends RouteBuilder {

    @Override
    public void configure() {
        from("kafka:{{app.kafka.topic-in}}")
                .transacted("serializablePolicy")
                .unmarshal()
                .json(InputMessage.class)
                .bean(AggregationService.class, "aggregate")
                .marshal()
                .json(JsonLibrary.Jackson)
                .to("kafka:{{app.kafka.topic-link}}");
    }
}

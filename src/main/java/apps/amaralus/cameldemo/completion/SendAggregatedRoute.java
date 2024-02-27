package apps.amaralus.cameldemo.completion;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
public class SendAggregatedRoute extends RouteBuilder {
    @Override
    public void configure() {
        from("direct:send-aggregated")
            .marshal()
            .json(JsonLibrary.Jackson)
            .to("kafka:{{app.kafka.topic-out}}");
    }
}

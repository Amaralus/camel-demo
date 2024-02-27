package apps.amaralus.cameldemo.completion;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import static org.apache.camel.LoggingLevel.INFO;

@Component
public class CronTriggerRoute extends RouteBuilder {
    @Override
    public void configure() {
        from("cron:tab?schedule={{app.aggregation-completion.cron-expression}}")
            .log(INFO, "Regular aggregation completion started")
            .to("direct:aggregation-completion");
    }
}

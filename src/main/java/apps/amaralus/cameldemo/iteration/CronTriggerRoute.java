package apps.amaralus.cameldemo.iteration;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class CronTriggerRoute extends RouteBuilder {
    @Override
    public void configure() {
        from("cron:tab?schedule={{app.aggregation-completion.cron-expression}}")
            .to("direct:aggregation-completion");
    }
}

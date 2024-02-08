package apps.amaralus.cameldemo.iteration;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class CronTriggerRoute extends RouteBuilder {
    @Override
    public void configure() {
        from("cron:tab?schedule=* 0 * * * *")
            .to("direct:aggregation-completion");
    }
}

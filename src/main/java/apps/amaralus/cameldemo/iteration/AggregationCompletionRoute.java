package apps.amaralus.cameldemo.iteration;

import apps.amaralus.cameldemo.iteration.lock.LockService;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.aggregate.GroupedBodyAggregationStrategy;
import org.springframework.stereotype.Component;

@Component
public class AggregationCompletionRoute extends RouteBuilder {

    private static final String NEXT_ITERATION_QUERY =
            "update demo.iteration_counter " +
            "set iteration = nextval('demo.iteration_seq') " +
            "where id = 1";
    private static final String FIND_AGGREGATED_DATA =
            "select * " +
            "from demo.aggregated_data " +
            "where iteration < (select iteration from demo.iteration_counter where id = 1) " +
            "  and aggregation_completed = false";

    private static final String CONFIRM_AGGREGATION_QUERY =
            "update demo.aggregated_data " +
            "set aggregation_completed = true " +
            "where id = '${header.aggregatedId}'";

    @Override
    public void configure() {
        // todo переделать на .sql()
        from("direct:aggregation-completion")
            .transacted("serializablePolicy")
                .bean(LockService.class, "tryLock")
                .filter(body().isEqualTo(true))
                .setBody(constant(NEXT_ITERATION_QUERY))
                .to("jdbc:dataSource?transacted=true")
                // задержка на 5 секунд для предотвращения
                // попадания одних и тех же данных в разные периоды агрегации
                // в случае сбоя в этот момент
                .delay(simple("{{app.aggregation-completion.post-iteration-delay-ms}}"))
                .asyncDelayed()
                .setBody(constant(FIND_AGGREGATED_DATA))
                .to("jdbc:dataSource")
                .setHeader("found", body().method("size"))
            .split(body())
                .bean(AggregatedDataMapper.class, "map")
                .setHeader("aggregatedId", body().method("getId"))
                .to("direct:send-aggregated")
                .setBody(simple(CONFIRM_AGGREGATION_QUERY))
                .to("jdbc:dataSource?transacted=true")
                // собираем все сообщения и освобождаем lock
                .aggregate(new GroupedBodyAggregationStrategy()).constant(true)
                    .completionSize(header("found"))
                    .bean(LockService.class, "unlock");
    }
}

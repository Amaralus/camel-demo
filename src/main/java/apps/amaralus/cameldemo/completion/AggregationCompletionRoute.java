package apps.amaralus.cameldemo.completion;

import apps.amaralus.cameldemo.completion.lock.LockService;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.aggregate.GroupedBodyAggregationStrategy;
import org.springframework.stereotype.Component;

@Component
public class AggregationCompletionRoute extends RouteBuilder {

    private static final String NEXT_ITERATION_QUERY =
            "update {{app.database.schema-name}}.iteration_counter " +
            "set iteration = nextval('{{app.database.schema-name}}.iteration_seq') " +
            "where id = 1";
    private static final String FIND_AGGREGATED_DATA =
            "select * " +
            "from {{app.database.schema-name}}.aggregated_data " +
            "where iteration < (select iteration from {{app.database.schema-name}}.iteration_counter where id = 1) " +
            "and aggregation_completed = false";

    private static final String CONFIRM_AGGREGATION_QUERY =
            "update {{app.database.schema-name}}.aggregated_data " +
            "set aggregation_completed = true " +
            "where id = '${header.aggregatedId}'";

    private static final String JDBC_URL = "jdbc:dataSource?transacted=true";

    @Override
    public void configure() {
        from("direct:aggregation-completion")
            .transacted("serializablePolicy")
            .bean(LockService.class, "tryLock")
            .filter(body().isEqualTo(true))
            .setBody(simple(NEXT_ITERATION_QUERY))
            .to(JDBC_URL)
            // задержка на 5 секунд для предотвращения
            // попадания одних и тех же данных в разные периоды агрегации
            // в случае сбоя в этот момент
            .delay(simple("{{app.aggregation-completion.post-iteration-delay-ms}}")).asyncDelayed()
            .setBody(simple(FIND_AGGREGATED_DATA))
            .to(JDBC_URL)
            .setHeader("found", body().method("size"))
            .split(body())
            .bean(AggregatedDataMapper.class, "map")
            .setHeader("aggregatedId", body().method("getId"))
            .to("direct:send-aggregated")
            .setBody(simple(CONFIRM_AGGREGATION_QUERY))
            .to(JDBC_URL)
            // собираем все сообщения и освобождаем lock
            .aggregate(new GroupedBodyAggregationStrategy()).constant(true)
            .completionSize(header("found"))
            .bean(LockService.class, "unlock");
    }
}

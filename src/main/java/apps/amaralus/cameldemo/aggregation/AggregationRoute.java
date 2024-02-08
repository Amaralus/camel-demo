package apps.amaralus.cameldemo.aggregation;

import apps.amaralus.cameldemo.aggregation.model.AggregationLink;
import apps.amaralus.cameldemo.aggregation.model.InputMessage;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class AggregationRoute extends RouteBuilder {
    private static final String QUERY = """
            insert into demo.aggregated_data (iteration, group_field_1, group_field_2, aggregation_result)
            values (
            (select iteration from demo.iteration_counter where id = 1),
            '${body.groupField1()}',
            ${body.groupField2()},
            ${body.number()})
            on conflict on constraint iteration_group do update
                set aggregation_result = demo.aggregated_data.aggregation_result + ${body.number()},
                    updated_at         = now()
            returning id;
            """;

    @Override
    public void configure() {
        from("kafka:{{app.kafka.topic-in}}")
                .transacted("serializablePolicy")
                .unmarshal()
                .json(InputMessage.class)
                .to("bean-validator://x")
                .split(body().method("payload"))
                .streaming()
                .filter(body().method("type").isEqualTo("A"))
                .setHeader("payloadId", body().method("id"))
                .setBody(simple(QUERY))
                .to("jdbc:dataSource?transacted=true&outputType=SelectOne")
                .bean(LinkTransformer.class, "transform")
                .marshal()
                .json(AggregationLink.class)
                .to("kafka:{{app.kafka.topic-link}}");
    }
}

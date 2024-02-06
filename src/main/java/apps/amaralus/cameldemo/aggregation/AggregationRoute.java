package apps.amaralus.cameldemo.aggregation;

import apps.amaralus.cameldemo.aggregation.model.InputMessage;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.stereotype.Component;

@Component
public class AggregationRoute extends RouteBuilder {

    private static final String QUERY = """
            insert into demo.aggregated_data (iteration, group_field_1, group_field_2, aggregation_result, aggregated_ids)
            values (
            (select iteration from demo.iteration_counter where id = 1),
            '${body.groupField1()}',
            ${body.groupField2()},
            ${body.number()},
            array [${body.id()}])
            on conflict on constraint iteration_group do update
                set aggregation_result = demo.aggregated_data.aggregation_result + ${body.number()},
                    aggregated_ids     = array_append(demo.aggregated_data.aggregated_ids, ${body.id()}),
                    updated_at         = now();
            """;

    @Override
    public void configure() {
        from("kafka:{{app.kafka.topic-in}}")
            .unmarshal(new JacksonDataFormat(InputMessage.class))
            .to("bean-validator://x")
            .split(simple("${body.payload()}"))
                .streaming()
                .filter(simple("${body.type()} == 'A'"))
                .setBody(simple(QUERY))
                .to("spring-jdbc:dataSource");
    }
}

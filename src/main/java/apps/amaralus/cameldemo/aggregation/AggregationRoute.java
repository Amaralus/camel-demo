package apps.amaralus.cameldemo.aggregation;

import apps.amaralus.cameldemo.aggregation.model.AggregationLink;
import apps.amaralus.cameldemo.aggregation.model.InputMessage;
import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
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
                .unmarshal(new JacksonDataFormat(InputMessage.class))
                .to("bean-validator://x")
                .split(simple("${body.payload()}"))
                .streaming()
                .filter(simple("${body.type()} == 'A'"))
                .setHeader("payloadId", simple("${body.id()}"))
                .setBody(simple(QUERY))
                .to("jdbc:dataSource?transacted=true&outputType=SelectOne")
                .bean(LinkTransformer.class, "transform")
                .marshal(new JacksonDataFormat(AggregationLink.class))
                .to("kafka:{{app.kafka.topic-link}}");
    }
}

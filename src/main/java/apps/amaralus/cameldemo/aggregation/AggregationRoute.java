package apps.amaralus.cameldemo.aggregation;

import apps.amaralus.cameldemo.aggregation.model.AggregationLink;
import apps.amaralus.cameldemo.aggregation.model.InputMessage;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class AggregationRoute extends RouteBuilder {
    private static final String QUERY =
            "insert into demo.aggregated_data (iteration, group_field_1, group_field_2, aggregation_result)\n" +
            "values (\n" +
            "(select iteration from demo.iteration_counter where id = 1),\n" +
            "'${body.getGroupField1()}',\n" +
            "${body.getGroupField2()},\n" +
            "${body.getNumber()})\n" +
            "on conflict on constraint iteration_group do update\n" +
            "    set aggregation_result = demo.aggregated_data.aggregation_result + ${body.getNumber()},\n" +
            "        updated_at         = now()\n" +
            "returning id;\n";

    @Override
    public void configure() {
        from("kafka:{{app.kafka.topic-in}}")
                .transacted("serializablePolicy")
                .unmarshal()
                .json(InputMessage.class)
                .to("bean-validator://x")
                .split(body().method("getPayload"))
                .streaming()
                .filter(body().method("getType").isEqualTo("A"))
                .setHeader("payloadId", body().method("getId"))
                .setBody(simple(QUERY))
                .to("jdbc:dataSource?transacted=true&outputType=SelectOne")
                .bean(LinkTransformer.class, "transform")
                .marshal()
                .json(AggregationLink.class)
                .to("kafka:{{app.kafka.topic-link}}");
    }
}

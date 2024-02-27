package apps.amaralus.cameldemo.aggregation;

import apps.amaralus.cameldemo.aggregation.model.AggregatedPayloadData;
import apps.amaralus.cameldemo.aggregation.model.InputMessage;
import apps.amaralus.cameldemo.aggregation.model.PayloadData;
import apps.amaralus.cameldemo.completion.model.AggregatedData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Body;
import org.apache.camel.FluentProducerTemplate;
import org.apache.camel.Header;
import org.apache.camel.component.kafka.consumer.KafkaManualCommit;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.stream.Collectors;

import static org.apache.camel.component.kafka.KafkaConstants.MANUAL_COMMIT;

@Service
@RequiredArgsConstructor
@Slf4j
public class AggregationService implements InitializingBean {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final FluentProducerTemplate producerTemplate;
    private String schemaName;
    private String query = "insert into %1$s.aggregated_data (iteration, group_field_1, group_field_2, aggregation_result) " +
                           "values (" +
                           "(select iteration from %1$s.iteration_counter where id = 1)," +
                           ":groupField1," +
                           ":groupField2," +
                           ":number) " +
                           "on conflict on constraint iteration_group do update " +
                           "set aggregation_result = %1$s.aggregated_data.aggregation_result + :number," +
                           "updated_at = now() " +
                           "returning id";

    @Override
    public void afterPropertiesSet() {
        query = String.format(query, schemaName);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    public InputMessage aggregate(@Body InputMessage inputMessage,
                                  @Header(MANUAL_COMMIT) KafkaManualCommit manualCommit) {
        log.info("{}", inputMessage);
        inputMessage.setPayload(inputMessage.getPayload().stream()
                .map(this::handlePayload)
                .collect(Collectors.toList()));
        manualCommit.commit();
        return inputMessage;
    }

    private PayloadData handlePayload(PayloadData payload) {
        switch (payload.getType()) {
            case DONT_AGGREGATE:
                return new AggregatedPayloadData(payload, null);
            case AGGREGATE: {
                var aggregationId = jdbcTemplate.queryForObject(query, new BeanPropertySqlParameterSource(payload), UUID.class);
                return new AggregatedPayloadData(payload, aggregationId);
            }
            case AGGREGATE_FAST: {
                var aggregationId = UUID.randomUUID();
                producerTemplate.withBody(new AggregatedData(aggregationId, payload))
                        .to("direct:send-aggregated")
                        .asyncSend();
                return new AggregatedPayloadData(payload, aggregationId);
            }
            default:
                throw new UnsupportedOperationException("Unexpected payload type: " + payload.getType());
        }
    }

    @Autowired
    public void setSchemaName(@Value("${app.database.schema-name}") String schemaName) {
        this.schemaName = schemaName;
    }
}

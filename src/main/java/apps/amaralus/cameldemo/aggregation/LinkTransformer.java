package apps.amaralus.cameldemo.aggregation;

import apps.amaralus.cameldemo.aggregation.model.AggregationLink;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Body;
import org.apache.camel.Header;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class LinkTransformer {

    public AggregationLink transform(@Header("payloadId") Long payloadId, @Body UUID aggregationId) {
        return new AggregationLink(payloadId, aggregationId);
    }
}

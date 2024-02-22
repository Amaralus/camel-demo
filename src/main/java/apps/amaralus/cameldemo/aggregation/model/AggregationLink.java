package apps.amaralus.cameldemo.aggregation.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class AggregationLink {
    private Long payloadId;
    private UUID aggregationId;
}

package apps.amaralus.cameldemo.aggregation.model;

import java.util.UUID;

public record AggregationLink(Long payloadId, UUID aggregationId) {
}

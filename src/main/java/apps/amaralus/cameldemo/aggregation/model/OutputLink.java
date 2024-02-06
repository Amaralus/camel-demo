package apps.amaralus.cameldemo.aggregation.model;

import java.util.UUID;

public record OutputLink(
        long inputId,
        UUID aggregateId
) {
}

package apps.amaralus.cameldemo.iteration.model;

import java.util.UUID;

public record AggregatedData(
        UUID id,
        String groupField1,
        Long groupField2,
        Integer aggregationResult
) {
}

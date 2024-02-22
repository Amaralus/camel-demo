package apps.amaralus.cameldemo.iteration.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class AggregatedData {
        private UUID id;
        private String groupField1;
        private Long groupField2;
        private Integer aggregationResult;
}

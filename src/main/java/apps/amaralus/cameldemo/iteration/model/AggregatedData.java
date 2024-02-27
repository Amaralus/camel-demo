package apps.amaralus.cameldemo.iteration.model;

import apps.amaralus.cameldemo.aggregation.model.PayloadData;
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

    public AggregatedData(UUID id, PayloadData payloadData) {
        this.id = id;
        groupField1 = payloadData.getGroupField1();
        groupField2 = payloadData.getGroupField2();
        aggregationResult = payloadData.getNumber();
    }
}

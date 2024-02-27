package apps.amaralus.cameldemo.aggregation.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class AggregatedPayloadData extends PayloadData {
    private UUID aggregatedId;

    public AggregatedPayloadData(PayloadData payloadData,
                                 UUID aggregatedId) {
        super(payloadData.getId(), payloadData.getType(), payloadData.getNumber(), payloadData.getGroupField1(), payloadData.getGroupField2());
        this.aggregatedId = aggregatedId;
    }
}

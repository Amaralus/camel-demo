package apps.amaralus.cameldemo.completion;

import apps.amaralus.cameldemo.completion.model.AggregatedData;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class AggregatedDataMapper {

    public AggregatedData map(Map<String, Object> values) {
        return new AggregatedData(
                (UUID) values.get("id"),
                (String) values.get("group_field_1"),
                (Long) values.get("group_field_2"),
                (Integer) values.get("aggregation_result")
        );
    }
}

package apps.amaralus.cameldemo.iteration;

import apps.amaralus.cameldemo.iteration.model.AggregatedData;
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

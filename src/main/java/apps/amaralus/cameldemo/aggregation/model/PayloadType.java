package apps.amaralus.cameldemo.aggregation.model;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.NUMBER)
public enum PayloadType {
    DONT_AGGREGATE,
    AGGREGATE,
    AGGREGATE_FAST
}

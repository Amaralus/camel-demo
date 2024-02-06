package apps.amaralus.cameldemo.aggregation.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PayloadData(
        @NotNull @Positive Long id,
        @NotBlank String type,
        @NotNull Integer number,
        @NotBlank String groupField1,
        @NotNull Long groupField2
) {
}

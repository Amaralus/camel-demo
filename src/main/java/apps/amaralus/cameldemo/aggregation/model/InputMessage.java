package apps.amaralus.cameldemo.aggregation.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record InputMessage(
        @NotBlank String id,
        @NotEmpty List<@Valid PayloadData> payload
) {
}

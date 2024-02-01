package apps.amaralus.cameldemo;

import jakarta.validation.constraints.NotBlank;

public record SomeData(long id, @NotBlank String text) {
}

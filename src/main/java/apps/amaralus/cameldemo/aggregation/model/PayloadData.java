package apps.amaralus.cameldemo.aggregation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayloadData {
        @NotNull
        @Positive
        private Long id;
    @NotNull
    private PayloadType type;
        @NotNull
        private Integer number;
        @NotBlank
        private String groupField1;
        @NotNull
        private Long groupField2;
}

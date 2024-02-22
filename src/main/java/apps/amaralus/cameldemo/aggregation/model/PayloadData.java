package apps.amaralus.cameldemo.aggregation.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class PayloadData {
        @NotNull
        @Positive
        private Long id;
        @NotBlank
        private String type;
        @NotNull
        private Integer number;
        @NotBlank
        private String groupField1;
        @NotNull
        private Long groupField2;
}

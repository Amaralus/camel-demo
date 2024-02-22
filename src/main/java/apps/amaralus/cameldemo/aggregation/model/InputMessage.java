package apps.amaralus.cameldemo.aggregation.model;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class InputMessage {
        @NotBlank
        private String id;
        @NotEmpty
        private List<@Valid PayloadData> payload;

}

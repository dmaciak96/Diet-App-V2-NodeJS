package pl.daveproject.dietapp.product.model.parameter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductParameterRequest {

    private UUID id;

    @NotBlank(message = "Product parameter name cannot be blank")
    @Size(min = 1, max = 255, message = "Product parameter name should be between 1 and 255")
    private String name;

    @NotBlank(message = "Product parameter value cannot be blank")
    @Size(min = 1, max = 255, message = "Product parameter value should be between 1 and 255")
    private String value;
}

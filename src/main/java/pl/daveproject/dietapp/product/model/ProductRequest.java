package pl.daveproject.dietapp.product.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.daveproject.dietapp.product.model.parameter.ProductParameterRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    private UUID id;

    @NotBlank(message = "Product name cannot be blank")
    @Size(min = 1, max = 255, message = "Product name should be between 1 and 255")
    private String name;

    @NotNull(message = "Product type cannot be null")
    private ProductType type;

    @NotNull(message = "Product kcal cannot be null")
    @Min(value = 0, message = "Product kcal cannot be less than 0")
    private Double kcal;

    @Valid
    private List<ProductParameterRequest> parameters = new ArrayList<>();
}

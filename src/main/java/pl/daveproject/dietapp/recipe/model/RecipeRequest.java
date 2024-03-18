package pl.daveproject.dietapp.recipe.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.daveproject.dietapp.recipe.model.productentry.RecipeProductEntryRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecipeRequest {

    private UUID id;

    @NotBlank(message = "Recipe name cannot be blank")
    @Size(min = 1, max = 255, message = "Recipe name should be between 1 and 255")
    private String name;

    @NotBlank(message = "Recipe description cannot be blank")
    @Size(min = 1, max = 2550, message = "Recipe description should be between 1 and 2550")
    private String description;

    @NotNull(message = "Recipe type cannot be null")
    private RecipeType type;

    @NotNull(message = "Recipe products list cannot be null")
    private List<RecipeProductEntryRequest> products = new ArrayList<>();
}
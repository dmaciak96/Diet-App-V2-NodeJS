package pl.daveproject.webdiet.shoppinglist.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.daveproject.webdiet.recipe.model.RecipeType;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingListRandomizeRequest {

    @NotNull(message = "Recipe types cannot be null")
    private List<RecipeType> recipeTypes = new ArrayList<>();

}

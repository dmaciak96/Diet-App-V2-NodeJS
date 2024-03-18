package pl.daveproject.dietapp.shoppinglist.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.daveproject.dietapp.recipe.model.RecipeDto;
import pl.daveproject.dietapp.shoppinglist.model.productentry.ShoppingListProductEntryDto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingListDto {
    private UUID id;
    private String name;
    private List<RecipeDto> recipes = new ArrayList<>();
    private List<ShoppingListProductEntryDto> products = new ArrayList<>();

    public String toEmailText() {
        var stringBuilder = new StringBuilder("%s\n".formatted(name));
        products.stream()
                .map(product -> "%s %s g\n".formatted(product.getProduct().getName(), product.getAmountInGrams()))
                .forEach(stringBuilder::append);
        return stringBuilder.toString();
    }

    public double getKcal() {
        if (recipes == null || recipes.isEmpty()) {
            return 0.0;
        }
        var kcal = recipes.stream()
                .map(RecipeDto::getKcal)
                .reduce(Double::sum)
                .orElse(0.0);
        return (double) Math.round(kcal * 100) / 100;
    }
}

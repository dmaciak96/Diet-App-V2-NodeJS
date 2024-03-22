package pl.daveproject.dietapp.recipe.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.daveproject.dietapp.UnitConverter;
import pl.daveproject.dietapp.recipe.model.productentry.RecipeProductEntryDto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecipeDto {

    private UUID id;
    private String name;
    private String description;
    private RecipeType type;
    private List<RecipeProductEntryDto> products = new ArrayList<>();

    public double getKcal() {
        if (products == null || products.isEmpty()) {
            return 0.0;
        }
        return products.stream()
                .map(this::getKcalForProduct)
                .reduce(Double::sum)
                .orElse(0.0);
    }

    public double getRoundedKcal() {
        return UnitConverter.roundToTwoDecimalDigits(getKcal());
    }

    private double getKcalForProduct(RecipeProductEntryDto recipeProductEntry) {
        if (recipeProductEntry == null || recipeProductEntry.getProduct() == null
                || recipeProductEntry.getProduct().getKcal() == null || recipeProductEntry.getAmountInGrams() == null) {
            return 0.0;
        }
        return (recipeProductEntry.getAmountInGrams()
                * recipeProductEntry.getProduct().getKcal()) / 100;
    }
}

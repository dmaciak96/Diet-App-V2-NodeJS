package pl.daveproject.dietapp.recipe.model.productentry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.daveproject.dietapp.product.model.ProductDto;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecipeProductEntryDto {

    private UUID id;
    private ProductDto product;
    private Double amountInGrams;

    public double getRoundedAmountInGrams() {
        if (amountInGrams == null) {
            return 0;
        }
        return (double) Math.round(amountInGrams * 100) / 100;
    }
}

package pl.daveproject.webdiet.shoppinglist.model.productentry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.daveproject.webdiet.product.model.ProductDto;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingListProductEntryDto {
    private UUID id;
    private Double amountInGrams;
    private ProductDto product;

    public double getRoundedAmountInGrams() {
        if (amountInGrams == null) {
            return 0;
        }
        return (double) Math.round(amountInGrams * 100) / 100;
    }
}

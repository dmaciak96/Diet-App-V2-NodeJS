package pl.daveproject.dietapp.shoppinglist.model.productentry;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
public class ShoppingListProductEntryRequest {

    @NotNull(message = "Product ID cannot be null")
    private UUID productId;

    @NotNull(message = "Amount in grams cannot be null")
    @Min(value = 0, message = "Amount in grams cannot be less than 0")
    private Double amountInGrams;
}

package pl.daveproject.dietapp.shoppinglist.model.productentry;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import pl.daveproject.dietapp.product.model.ProductBackupDto;

import java.util.UUID;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ShoppingListProductEntryBackupDto(UUID id,
                                                Double amountInGrams,
                                                ProductBackupDto product) {
}

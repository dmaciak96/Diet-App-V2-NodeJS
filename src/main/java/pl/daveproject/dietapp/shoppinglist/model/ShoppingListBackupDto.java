package pl.daveproject.dietapp.shoppinglist.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import pl.daveproject.dietapp.recipe.model.RecipeBackupDto;
import pl.daveproject.dietapp.shoppinglist.model.productentry.ShoppingListProductEntryBackupDto;

import java.util.List;
import java.util.UUID;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ShoppingListBackupDto(UUID id,
                                    String name,
                                    List<RecipeBackupDto> recipes,
                                    List<ShoppingListProductEntryBackupDto> products) {
}

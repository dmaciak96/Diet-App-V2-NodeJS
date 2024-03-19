package pl.daveproject.dietapp.recipe.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import pl.daveproject.dietapp.recipe.model.productentry.RecipeProductEntryBackupDto;

import java.util.List;
import java.util.UUID;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record RecipeBackupDto(UUID id,
                              String name,
                              String description,
                              RecipeType type,
                              List<RecipeProductEntryBackupDto> products) {
}

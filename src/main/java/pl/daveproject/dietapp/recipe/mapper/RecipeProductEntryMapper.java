package pl.daveproject.dietapp.recipe.mapper;

import org.mapstruct.Mapper;
import pl.daveproject.dietapp.product.mapper.ProductMapper;
import pl.daveproject.dietapp.recipe.model.productentry.RecipeProductEntry;
import pl.daveproject.dietapp.recipe.model.productentry.RecipeProductEntryDto;

import java.util.List;

@Mapper(uses = ProductMapper.class)
public interface RecipeProductEntryMapper {
    RecipeProductEntryDto entityToDto(RecipeProductEntry recipeProductEntry);

    List<RecipeProductEntryDto> entitiesToDtoList(List<RecipeProductEntry> recipeProductEntries);

    RecipeProductEntry dtoToEntity(RecipeProductEntryDto recipeProductEntryDto);

    List<RecipeProductEntry> dtoListToEntities(List<RecipeProductEntryDto> recipeProductEntryDtoList);
}

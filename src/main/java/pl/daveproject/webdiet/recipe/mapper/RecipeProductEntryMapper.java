package pl.daveproject.webdiet.recipe.mapper;

import org.mapstruct.Mapper;
import pl.daveproject.webdiet.product.mapper.ProductMapper;
import pl.daveproject.webdiet.recipe.model.productentry.RecipeProductEntry;
import pl.daveproject.webdiet.recipe.model.productentry.RecipeProductEntryDto;

import java.util.List;

@Mapper(uses = ProductMapper.class)
public interface RecipeProductEntryMapper {
    RecipeProductEntryDto entityToDto(RecipeProductEntry recipeProductEntry);

    List<RecipeProductEntryDto> entitiesToDtoList(List<RecipeProductEntry> recipeProductEntries);

    RecipeProductEntry dtoToEntity(RecipeProductEntryDto recipeProductEntryDto);

    List<RecipeProductEntry> dtoListToEntities(List<RecipeProductEntryDto> recipeProductEntryDtoList);
}

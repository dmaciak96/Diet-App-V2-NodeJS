package pl.daveproject.webdiet.recipe.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.daveproject.webdiet.recipe.model.Recipe;
import pl.daveproject.webdiet.recipe.model.RecipeDto;
import pl.daveproject.webdiet.recipe.model.RecipeRequest;

import java.util.List;

@Mapper(uses = RecipeProductEntryMapper.class)
public interface RecipeMapper {

    RecipeDto entityToDto(Recipe recipe);

    List<RecipeDto> entitiesToDtoList(List<Recipe> recipes);

    @Mapping(target = "products", ignore = true)
    RecipeDto requestToDto(RecipeRequest request);

    Recipe dtoToEntity(RecipeDto recipeDto);

    RecipeRequest dtoToRequest(RecipeDto recipeDto);
}

package pl.daveproject.dietapp.shoppinglist.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.daveproject.dietapp.recipe.mapper.RecipeMapper;
import pl.daveproject.dietapp.shoppinglist.model.ShoppingList;
import pl.daveproject.dietapp.shoppinglist.model.ShoppingListDto;
import pl.daveproject.dietapp.shoppinglist.model.ShoppingListRequest;

import java.util.List;

@Mapper(uses = {ShoppingListProductEntryMapper.class, RecipeMapper.class})
public interface ShoppingListMapper {
    ShoppingListDto entityToDto(ShoppingList shoppingList);

    List<ShoppingListDto> entitiesToDtoList(List<ShoppingList> shoppingLists);

    @Mapping(target = "recipes", ignore = true)
    ShoppingListDto requestToDto(ShoppingListRequest request);

    ShoppingList dtoToEntity(ShoppingListDto shoppingListDto);
}

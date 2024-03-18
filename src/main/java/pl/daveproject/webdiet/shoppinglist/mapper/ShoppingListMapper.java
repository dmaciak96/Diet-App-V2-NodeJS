package pl.daveproject.webdiet.shoppinglist.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.daveproject.webdiet.recipe.mapper.RecipeMapper;
import pl.daveproject.webdiet.shoppinglist.model.ShoppingList;
import pl.daveproject.webdiet.shoppinglist.model.ShoppingListDto;
import pl.daveproject.webdiet.shoppinglist.model.ShoppingListRequest;

import java.util.List;

@Mapper(uses = {ShoppingListProductEntryMapper.class, RecipeMapper.class})
public interface ShoppingListMapper {
    ShoppingListDto entityToDto(ShoppingList shoppingList);

    List<ShoppingListDto> entitiesToDtoList(List<ShoppingList> shoppingLists);

    @Mapping(target = "recipes", ignore = true)
    ShoppingListDto requestToDto(ShoppingListRequest request);

    ShoppingList dtoToEntity(ShoppingListDto shoppingListDto);
}

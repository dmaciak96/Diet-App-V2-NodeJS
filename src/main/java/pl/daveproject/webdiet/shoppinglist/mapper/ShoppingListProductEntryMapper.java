package pl.daveproject.webdiet.shoppinglist.mapper;

import org.mapstruct.Mapper;
import pl.daveproject.webdiet.product.mapper.ProductMapper;
import pl.daveproject.webdiet.shoppinglist.model.productentry.ShoppingListProductEntry;
import pl.daveproject.webdiet.shoppinglist.model.productentry.ShoppingListProductEntryDto;

import java.util.List;

@Mapper(uses = ProductMapper.class)
public interface ShoppingListProductEntryMapper {
    ShoppingListProductEntryDto entityToDto(ShoppingListProductEntry shoppingListProductEntry);

    List<ShoppingListProductEntryDto> entitiesToDtoList(List<ShoppingListProductEntry> shoppingListProductEntries);

    ShoppingListProductEntry dtoToEntity(ShoppingListProductEntryDto shoppingListProductEntryDto);

    List<ShoppingListProductEntry> dtoListToEntities(List<ShoppingListProductEntryDto> shoppingListProductEntryDtoList);
}

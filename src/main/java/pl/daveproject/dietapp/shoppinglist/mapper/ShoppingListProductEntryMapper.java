package pl.daveproject.dietapp.shoppinglist.mapper;

import org.mapstruct.Mapper;
import pl.daveproject.dietapp.product.mapper.ProductMapper;
import pl.daveproject.dietapp.shoppinglist.model.productentry.ShoppingListProductEntry;
import pl.daveproject.dietapp.shoppinglist.model.productentry.ShoppingListProductEntryDto;

import java.util.List;

@Mapper(uses = ProductMapper.class)
public interface ShoppingListProductEntryMapper {
    ShoppingListProductEntryDto entityToDto(ShoppingListProductEntry shoppingListProductEntry);

    List<ShoppingListProductEntryDto> entitiesToDtoList(List<ShoppingListProductEntry> shoppingListProductEntries);

    ShoppingListProductEntry dtoToEntity(ShoppingListProductEntryDto shoppingListProductEntryDto);

    List<ShoppingListProductEntry> dtoListToEntities(List<ShoppingListProductEntryDto> shoppingListProductEntryDtoList);
}

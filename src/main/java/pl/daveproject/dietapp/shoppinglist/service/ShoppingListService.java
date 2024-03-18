package pl.daveproject.dietapp.shoppinglist.service;


import pl.daveproject.dietapp.shoppinglist.model.ShoppingListDto;
import pl.daveproject.dietapp.shoppinglist.model.ShoppingListRandomizeRequest;
import pl.daveproject.dietapp.shoppinglist.model.ShoppingListRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShoppingListService {
    List<ShoppingListDto> findAll();

    Optional<ShoppingListDto> findById(UUID id);

    ShoppingListDto save(ShoppingListRequest request);

    ShoppingListDto save(ShoppingListRandomizeRequest randomizeRequest);

    ShoppingListDto update(UUID id, ShoppingListRequest request);

    void delete(ShoppingListDto shoppingListDto);
}

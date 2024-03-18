package pl.daveproject.webdiet.shoppinglist.service;


import pl.daveproject.webdiet.shoppinglist.model.ShoppingListDto;
import pl.daveproject.webdiet.shoppinglist.model.ShoppingListRandomizeRequest;
import pl.daveproject.webdiet.shoppinglist.model.ShoppingListRequest;

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

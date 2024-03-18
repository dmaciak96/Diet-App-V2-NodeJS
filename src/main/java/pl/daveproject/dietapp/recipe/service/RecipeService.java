package pl.daveproject.dietapp.recipe.service;

import pl.daveproject.dietapp.product.model.ProductDto;
import pl.daveproject.dietapp.recipe.model.RecipeDto;
import pl.daveproject.dietapp.recipe.model.RecipeRequest;
import pl.daveproject.dietapp.recipe.model.RecipeType;
import pl.daveproject.dietapp.recipe.model.productentry.RecipeProductEntryRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RecipeService {
    List<RecipeDto> findAll();

    Optional<RecipeDto> findById(UUID id);

    List<RecipeDto> findAllByType(RecipeType recipeType);

    RecipeDto save(RecipeRequest request);

    RecipeDto update(UUID id, RecipeRequest request);

    void delete(RecipeDto recipeDto);

    List<ProductDto> getNotExistingProducts(List<RecipeProductEntryRequest> recipeProductEntries);

    boolean isAllProductExists(List<RecipeProductEntryRequest> recipeProductEntries);
}

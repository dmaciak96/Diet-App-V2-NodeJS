package pl.daveproject.dietapp.shoppinglist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.daveproject.dietapp.exception.RecipeNotExistsException;
import pl.daveproject.dietapp.recipe.model.RecipeType;
import pl.daveproject.dietapp.recipe.model.productentry.RecipeProductEntryDto;
import pl.daveproject.dietapp.recipe.service.RecipeService;
import pl.daveproject.dietapp.security.service.UserService;
import pl.daveproject.dietapp.shoppinglist.mapper.ShoppingListMapper;
import pl.daveproject.dietapp.shoppinglist.model.ShoppingListDto;
import pl.daveproject.dietapp.shoppinglist.model.ShoppingListRandomizeRequest;
import pl.daveproject.dietapp.shoppinglist.model.ShoppingListRequest;
import pl.daveproject.dietapp.shoppinglist.model.productentry.ShoppingListProductEntryDto;
import pl.daveproject.dietapp.shoppinglist.repository.ShoppingListRepository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.stream.Collectors.groupingBy;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShoppingListServiceImpl implements ShoppingListService {

    private final ShoppingListRepository shoppingListRepository;
    private final ShoppingListMapper shoppingListMapper;
    private final RecipeService recipeService;
    private final UserService userService;

    @Override
    public List<ShoppingListDto> findAll() {
        var currentUser = userService.getCurrentUser();
        log.debug("Find all shopping lists for user {}", currentUser.getEmail());
        var entities = shoppingListRepository.findAllByApplicationUserId(currentUser.getId());
        log.debug("Mapping {} entities to dto", entities.size());
        return shoppingListMapper.entitiesToDtoList(entities);
    }

    @Override
    public Optional<ShoppingListDto> findById(UUID id) {
        var currentUser = userService.getCurrentUser();
        log.debug("Find shopping list by id {} for user {}", id, currentUser.getEmail());
        var shoppingList = shoppingListRepository.findFirstByApplicationUserIdAndId(currentUser.getId(), id);
        return shoppingList.map(shoppingListMapper::entityToDto);
    }

    @Override
    public ShoppingListDto save(ShoppingListRequest request) {
        var currentUser = userService.getCurrentUser();
        var dtoToCreate = shoppingListMapper.requestToDto(request);
        addRecipesToDto(request.getRecipes(), dtoToCreate);
        addProductsFromDtoRecipes(dtoToCreate);

        var entityToCreate = shoppingListMapper.dtoToEntity(dtoToCreate);
        entityToCreate.getProducts().forEach(product -> product.setShoppingList(entityToCreate));
        entityToCreate.setApplicationUser(currentUser);

        var savedEntity = shoppingListRepository.save(entityToCreate);
        log.debug("Saved new shopping list {} - {}", savedEntity.getName(), savedEntity.getId());
        return shoppingListMapper.entityToDto(savedEntity);
    }

    @Override
    public ShoppingListDto save(ShoppingListRandomizeRequest randomizeRequest) {
        var recipesUuids = randomizeRequest.getRecipeTypes().stream()
                .map(this::getRandomRecipeUuid)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        var name = "%s_%s".formatted(LocalDate.now(), UUID.randomUUID().toString());
        var shoppingListRequest = new ShoppingListRequest(null, name, recipesUuids);
        return save(shoppingListRequest);
    }

    private Optional<UUID> getRandomRecipeUuid(RecipeType recipeType) {
        var recipes = recipeService.findAllByType(recipeType);
        if (recipes.isEmpty()) {
            return Optional.empty();
        }
        Collections.shuffle(recipes);
        return Optional.of(recipes.get(0).getId());
    }

    @Override
    public ShoppingListDto update(UUID id, ShoppingListRequest request) {
        var currentUser = userService.getCurrentUser();
        var currentEntity = shoppingListRepository.findFirstByApplicationUserIdAndId(currentUser.getId(), id);

        var dtoToUpdate = shoppingListMapper.requestToDto(request);
        addRecipesToDto(request.getRecipes(), dtoToUpdate);
        addProductsFromDtoRecipes(dtoToUpdate);

        var entityToUpdate = shoppingListMapper.dtoToEntity(dtoToUpdate);
        entityToUpdate.getProducts().forEach(product -> product.setShoppingList(entityToUpdate));
        entityToUpdate.setApplicationUser(currentUser);

        entityToUpdate.setId(currentEntity.orElseThrow().getId());

        var updatedEntity = shoppingListRepository.save(entityToUpdate);
        return shoppingListMapper.entityToDto(updatedEntity);
    }

    private void addProductsFromDtoRecipes(ShoppingListDto dtoToUpdate) {
        var products = dtoToUpdate.getRecipes().stream()
                .flatMap(recipeDto -> recipeDto.getProducts().stream())
                .collect(groupingBy(e -> e.getProduct().getName()));

        var shoppingListProductEntries = products.values().stream()
                .map(recipeProductEntryDtoList -> {
                    var productDto = recipeProductEntryDtoList.get(0).getProduct();
                    var amountsInGram = sumAmountsInGrams(recipeProductEntryDtoList);
                    return ShoppingListProductEntryDto.builder()
                            .product(productDto)
                            .amountInGrams(amountsInGram)
                            .build();
                })
                .toList();

        dtoToUpdate.setProducts(shoppingListProductEntries);
    }

    private double sumAmountsInGrams(List<RecipeProductEntryDto> recipeProductEntryDtoList) {
        return recipeProductEntryDtoList.stream()
                .map(RecipeProductEntryDto::getAmountInGrams)
                .reduce(0.0, Double::sum);
    }

    private void addRecipesToDto(List<UUID> recipes, ShoppingListDto dtoToUpdate) {
        var recipeDtoList = recipes.stream()
                .map(id -> {
                    var recipe = recipeService.findById(id);
                    if (recipe.isEmpty()) {
                        throw new RecipeNotExistsException(id);
                    }
                    return recipe.get();
                })
                .toList();

        dtoToUpdate.setRecipes(recipeDtoList);
    }

    @Override
    public void delete(ShoppingListDto shoppingListDto) {
        var currentUser = userService.getCurrentUser();
        if (shoppingListRepository.findFirstByApplicationUserIdAndId(currentUser.getId(),
                shoppingListDto.getId()).isPresent()) {
            log.debug("Deleting recipe {} - {}", shoppingListDto.getName(), shoppingListDto.getId());
            shoppingListRepository.deleteById(shoppingListDto.getId());
        }
    }
}

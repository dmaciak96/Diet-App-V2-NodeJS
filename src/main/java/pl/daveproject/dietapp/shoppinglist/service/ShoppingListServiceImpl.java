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
import pl.daveproject.dietapp.shoppinglist.model.ShoppingListFileDto;
import pl.daveproject.dietapp.shoppinglist.model.ShoppingListRandomizeRequest;
import pl.daveproject.dietapp.shoppinglist.model.ShoppingListRequest;
import pl.daveproject.dietapp.shoppinglist.model.productentry.ShoppingListProductEntryDto;
import pl.daveproject.dietapp.shoppinglist.repository.ShoppingListRepository;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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

    @Override
    public Optional<ShoppingListFileDto> exportToFileData(ShoppingListDto shoppingListDto) {
        try {
            var fileSuffix = ".txt";
            var filePrefix = shoppingListDto.getName();
            var tempFile = File.createTempFile(filePrefix, fileSuffix);
            log.debug("Temp file was created: {}", tempFile.getAbsolutePath());

            try (var writer = new BufferedWriter(new FileWriter(tempFile))) {
                writer.write(getShoppingListContent(shoppingListDto.getProducts()));
            }
            log.debug("Shopping list {} was exported to file", shoppingListDto.getId());

            byte[] fileBytes = Files.readAllBytes(tempFile.toPath());
            log.debug("Temp file was converted to byte array (size: {})", fileBytes.length);

            if (tempFile.delete()) {
                log.debug("Temp file {} was deleted", filePrefix + fileSuffix);
            } else {
                log.warn("Cannot remove temp file {}", filePrefix + fileSuffix);
            }
            return Optional.of(new ShoppingListFileDto(filePrefix + fileSuffix, fileBytes));
        } catch (IOException e) {
            log.error("Error during exporting shopping list {} to file data: {}", shoppingListDto.getId(), e.getMessage());
            return Optional.empty();
        }
    }

    private String getShoppingListContent(List<ShoppingListProductEntryDto> products) {
        var stringBuilder = new StringBuilder();
        products.forEach(product -> {
            stringBuilder.append("%s - %s[g]\n".formatted(product.getProduct().getName(), product.getAmountInGrams()));
        });
        return stringBuilder.toString();
    }
}

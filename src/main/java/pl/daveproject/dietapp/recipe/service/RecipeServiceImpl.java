package pl.daveproject.dietapp.recipe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.daveproject.dietapp.product.model.ProductDto;
import pl.daveproject.dietapp.product.service.ProductService;
import pl.daveproject.dietapp.recipe.mapper.RecipeMapper;
import pl.daveproject.dietapp.recipe.model.RecipeDto;
import pl.daveproject.dietapp.recipe.model.RecipeRequest;
import pl.daveproject.dietapp.recipe.model.RecipeType;
import pl.daveproject.dietapp.recipe.model.productentry.RecipeProductEntryDto;
import pl.daveproject.dietapp.recipe.model.productentry.RecipeProductEntryRequest;
import pl.daveproject.dietapp.recipe.repository.RecipeRepository;
import pl.daveproject.dietapp.security.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;
    private final ProductService productService;
    private final UserService userService;

    @Override
    public List<RecipeDto> findAll() {
        var currentUser = userService.getCurrentUser();
        log.debug("Find all recipes for user {}", currentUser.getEmail());
        var entities = recipeRepository.findAllByApplicationUserId(currentUser.getId());
        log.debug("Mapping {} entities to dto", entities.size());
        return recipeMapper.entitiesToDtoList(entities);
    }

    @Override
    public Optional<RecipeDto> findById(UUID id) {
        var currentUser = userService.getCurrentUser();
        log.debug("Find recipe by id {} for user {}", id, currentUser.getEmail());
        var recipe = recipeRepository.findFirstByApplicationUserIdAndId(currentUser.getId(), id);
        return recipe.map(recipeMapper::entityToDto);
    }

    @Override
    public List<RecipeDto> findAllByType(RecipeType recipeType) {
        var currentUser = userService.getCurrentUser();
        log.debug("Find all recipes by type {} for user {}", recipeType, currentUser.getEmail());
        var entities = recipeRepository.findAllByApplicationUserIdAndType(currentUser.getId(), recipeType);
        log.debug("Mapping {} entities to dto", entities.size());
        return recipeMapper.entitiesToDtoList(entities);
    }

    @Override
    public RecipeDto save(RecipeRequest request) {
        var currentUser = userService.getCurrentUser();
        var dtoToCreate = recipeMapper.requestToDto(request);
        addProductsToDto(request.getProducts(), dtoToCreate);

        var entityToCreate = recipeMapper.dtoToEntity(dtoToCreate);
        entityToCreate.getProducts().forEach(recipeProductEntry -> recipeProductEntry.setRecipe(entityToCreate));
        entityToCreate.setApplicationUser(currentUser);

        var savedEntity = recipeRepository.save(entityToCreate);
        log.debug("Saved new Recipe {} - {}", savedEntity.getName(), savedEntity.getId());
        return recipeMapper.entityToDto(savedEntity);
    }

    @Override
    public RecipeDto update(UUID id, RecipeRequest request) {
        var currentUser = userService.getCurrentUser();
        var currentEntity = recipeRepository.findFirstByApplicationUserIdAndId(currentUser.getId(), id);

        var dtoToUpdate = recipeMapper.requestToDto(request);
        addProductsToDto(request.getProducts(), dtoToUpdate);

        var entityToUpdate = recipeMapper.dtoToEntity(dtoToUpdate);
        entityToUpdate.getProducts().forEach(recipeProductEntry -> recipeProductEntry.setRecipe(entityToUpdate));
        entityToUpdate.setId(currentEntity.orElseThrow().getId());
        entityToUpdate.setApplicationUser(currentUser);

        var updatedEntity = recipeRepository.save(entityToUpdate);
        return recipeMapper.entityToDto(updatedEntity);
    }

    private void addProductsToDto(List<RecipeProductEntryRequest> products, RecipeDto recipeDto) {
        var productsEntryDtoList = products.stream()
                .filter(request -> productService.findByName(request.getProductName()).isPresent())
                .map(request -> RecipeProductEntryDto.builder()
                        .amountInGrams(request.getAmountInGrams())
                        .product(productService.findByName(request.getProductName()).get())
                        .build())
                .toList();

        log.debug("Set {} products to recipe {}", productsEntryDtoList.size(), recipeDto.getName());
        recipeDto.setProducts(productsEntryDtoList);
    }

    @Override
    public void delete(RecipeDto recipeDto) {
        var currentUser = userService.getCurrentUser();
        if (recipeRepository.findFirstByApplicationUserIdAndId(currentUser.getId(), recipeDto.getId()).isPresent()) {
            log.debug("Deleting recipe {} - {}", recipeDto.getName(), recipeDto.getId());
            recipeRepository.deleteById(recipeDto.getId());
        }
    }

    public List<ProductDto> getNotExistingProducts(List<RecipeProductEntryRequest> recipeProductEntries) {
        var allProductNames = getAllExistingProductNames();
        return recipeProductEntries.stream()
                .filter(recipeProductEntry -> !allProductNames.contains(
                        recipeProductEntry.getProductName().toLowerCase()))
                .map(recipeProductEntry -> ProductDto.builder()
                        .name(recipeProductEntry.getProductName())
                        .build())
                .toList();
    }

    public boolean isAllProductExists(List<RecipeProductEntryRequest> recipeProductEntries) {
        var allProductNames = getAllExistingProductNames();
        return recipeProductEntries.stream()
                .map(recipeProductEntry -> recipeProductEntry.getProductName().toLowerCase())
                .allMatch(allProductNames::contains);
    }

    private List<String> getAllExistingProductNames() {
        return productService.findAll()
                .stream()
                .map(e -> e.getName().toLowerCase())
                .toList();
    }
}

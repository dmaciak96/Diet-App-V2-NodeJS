package pl.daveproject.dietapp.backup.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.daveproject.dietapp.bmi.service.BmiService;
import pl.daveproject.dietapp.caloricneeds.service.TotalCaloricNeedsService;
import pl.daveproject.dietapp.exception.BackupException;
import pl.daveproject.dietapp.product.service.ProductService;
import pl.daveproject.dietapp.recipe.service.RecipeService;
import pl.daveproject.dietapp.shoppinglist.service.ShoppingListService;

@Slf4j
@Component
@RequiredArgsConstructor
public class BackupConverterImpl implements BackupConverter {

    private final ProductService productService;
    private final RecipeService recipeService;
    private final ShoppingListService shoppingListService;
    private final BmiService bmiService;
    private final TotalCaloricNeedsService caloricNeedsService;
    private final ObjectMapper objectMapper;

    @Override
    public byte[] convertProductsToByteArray() {
        var products = productService.findAll();
        log.info("Create backup data for {} products", products.size());
        return convertObjectToBytes(products);
    }

    @Override
    public byte[] convertRecipesToByteArray() {
        var recipes = recipeService.findAll();
        log.info("Create backup data for {} recipes", recipes.size());
        return convertObjectToBytes(recipes);
    }

    @Override
    public byte[] convertShoppingListsToByteArray() {
        var shoppingList = shoppingListService.findAll();
        log.info("Create backup data for {} shopping lists", shoppingList.size());
        return convertObjectToBytes(shoppingList);
    }

    @Override
    public byte[] convertBmiToByteArray() {
        var bmiData = bmiService.findAll();
        log.info("Create backup data for {} bmi entries", bmiData.size());
        return convertObjectToBytes(bmiData);
    }

    @Override
    public byte[] convertCaloricNeedsToByteArray() {
        var caloricNeeds = caloricNeedsService.findAll();
        log.info("Create backup data for {} total caloric needs entries", caloricNeeds.size());
        return convertObjectToBytes(caloricNeeds);
    }

    private <T> byte[] convertObjectToBytes(T object) {
        try {
            return objectMapper.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            throw new BackupException(e.getMessage());
        }
    }
}

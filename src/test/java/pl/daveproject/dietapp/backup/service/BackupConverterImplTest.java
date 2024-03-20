package pl.daveproject.dietapp.backup.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.daveproject.dietapp.DataProvider;
import pl.daveproject.dietapp.bmi.service.BmiService;
import pl.daveproject.dietapp.caloricneeds.service.TotalCaloricNeedsService;
import pl.daveproject.dietapp.product.service.ProductService;
import pl.daveproject.dietapp.recipe.service.RecipeService;
import pl.daveproject.dietapp.shoppinglist.service.ShoppingListService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BackupConverterImplTest {

    private final ProductService productService = mock(ProductService.class);
    private final RecipeService recipeService = mock(RecipeService.class);
    private final ShoppingListService shoppingListService = mock(ShoppingListService.class);
    private final BmiService bmiService = mock(BmiService.class);
    private final TotalCaloricNeedsService caloricNeedsService = mock(TotalCaloricNeedsService.class);
    private BackupConverter backupConverter;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setupMocks() {
        when(productService.findAll())
                .thenReturn(DataProvider.createProducts());
        when(recipeService.findAll())
                .thenReturn(DataProvider.createRecipes());
        when(shoppingListService.findAll())
                .thenReturn(DataProvider.createShoppingLists());
        when(bmiService.findAll())
                .thenReturn(DataProvider.createBmiEntries());
        when(caloricNeedsService.findAll())
                .thenReturn(DataProvider.createCaloricNeedsEntries());


        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        this.backupConverter = new BackupConverterImpl(productService,
                recipeService,
                shoppingListService,
                bmiService,
                caloricNeedsService,
                objectMapper);
    }

    @Test
    void convertProductsToByteArray() throws JsonProcessingException {
        var result = backupConverter.convertProductsToByteArray();
        var json = objectMapper.writeValueAsString(DataProvider.createProducts());
        assertThat(new String(result)).isEqualTo(json);
    }

    @Test
    void convertRecipesToByteArray() throws JsonProcessingException {
        var result = backupConverter.convertRecipesToByteArray();
        var json = objectMapper.writeValueAsString(DataProvider.createRecipes());
        assertThat(new String(result)).isEqualTo(json);
    }

    @Test
    void convertShoppingListsToByteArray() throws JsonProcessingException {
        var result = backupConverter.convertShoppingListsToByteArray();
        var json = objectMapper.writeValueAsString(DataProvider.createShoppingLists());
        assertThat(new String(result)).isEqualTo(json);
    }

    @Test
    void convertBmiToByteArray() throws JsonProcessingException {
        var result = backupConverter.convertBmiToByteArray();
        var json = objectMapper.writeValueAsString(DataProvider.createBmiEntries());
        assertThat(new String(result)).isEqualTo(json);
    }

    @Test
    void convertCaloricNeedsToByteArray() throws JsonProcessingException {
        var result = backupConverter.convertCaloricNeedsToByteArray();
        var json = objectMapper.writeValueAsString(DataProvider.createCaloricNeedsEntries());
        assertThat(new String(result)).isEqualTo(json);
    }
}
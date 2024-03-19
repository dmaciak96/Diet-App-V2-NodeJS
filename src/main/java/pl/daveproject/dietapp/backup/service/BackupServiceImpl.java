package pl.daveproject.dietapp.backup.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.daveproject.dietapp.bmi.service.BmiService;
import pl.daveproject.dietapp.caloricneeds.service.TotalCaloricNeedsService;
import pl.daveproject.dietapp.product.service.ProductService;
import pl.daveproject.dietapp.recipe.service.RecipeService;
import pl.daveproject.dietapp.shoppinglist.service.ShoppingListService;

@Slf4j
@Service
@RequiredArgsConstructor
public class BackupServiceImpl implements BackupService {

    private final ProductService productService;
    private final RecipeService recipeService;
    private final ShoppingListService shoppingListService;
    private final BmiService bmiService;
    private final TotalCaloricNeedsService caloricNeedsService;

    @Override
    public byte[] convertProductsToByteArray() {
        return new byte[0];
    }

    @Override
    public byte[] convertRecipesToByteArray() {
        return new byte[0];
    }

    @Override
    public byte[] convertShoppingListsToByteArray() {
        return new byte[0];
    }

    @Override
    public byte[] convertBmiToByteArray() {
        return new byte[0];
    }

    @Override
    public byte[] convertCaloricNeedsToByteArray() {
        return new byte[0];
    }
}

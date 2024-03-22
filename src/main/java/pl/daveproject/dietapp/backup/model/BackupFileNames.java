package pl.daveproject.dietapp.backup.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BackupFileNames {
    PRODUCTS("Products.json"),
    RECIPES("Recipes.json"),
    SHOPPING_LISTS("ShoppingLists.json"),
    BMI("Bmi.json"),
    TOTAL_CALORIC_NEEDS("TotalCaloricNeeds.json");

    private final String fileName;
}

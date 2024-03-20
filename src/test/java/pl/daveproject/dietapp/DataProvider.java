package pl.daveproject.dietapp;

import pl.daveproject.dietapp.bmi.model.BmiDto;
import pl.daveproject.dietapp.bmi.model.BmiRate;
import pl.daveproject.dietapp.bmi.model.UnitSystem;
import pl.daveproject.dietapp.caloricneeds.model.ActivityLevel;
import pl.daveproject.dietapp.caloricneeds.model.Gender;
import pl.daveproject.dietapp.caloricneeds.model.TotalCaloricNeedsDto;
import pl.daveproject.dietapp.product.model.ProductDto;
import pl.daveproject.dietapp.product.model.ProductType;
import pl.daveproject.dietapp.product.model.parameter.ProductParameterDto;
import pl.daveproject.dietapp.recipe.model.RecipeDto;
import pl.daveproject.dietapp.recipe.model.RecipeType;
import pl.daveproject.dietapp.recipe.model.productentry.RecipeProductEntryDto;
import pl.daveproject.dietapp.shoppinglist.model.ShoppingListDto;
import pl.daveproject.dietapp.shoppinglist.model.productentry.ShoppingListProductEntryDto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class DataProvider {

    public static List<ProductDto> createProducts() {
        return List.of(ProductDto.builder()
                        .name("testowy produkt 1")
                        .id(UUID.fromString("37abf227-91a4-4ee2-a5a8-c74a87c7db24"))
                        .kcal(12.123)
                        .type(ProductType.FATS)
                        .parameters(List.of(ProductParameterDto.builder()
                                .id(UUID.fromString("37abf227-91a4-4ee2-a5a8-c74a87c7db27"))
                                .name("parametr 1")
                                .value("wartość 1")
                                .build()))
                        .build(),
                ProductDto.builder()
                        .name("testowy produkt 2")
                        .id(UUID.fromString("37abf227-91a4-4ee2-a5a8-c74a87c7db25"))
                        .kcal(15.123)
                        .type(ProductType.GRAINS)
                        .parameters(List.of(ProductParameterDto.builder()
                                .id(UUID.fromString("37abf227-91a4-4ee2-a5a8-c74a87c7db28"))
                                .name("parametr 2")
                                .value("wartość 2")
                                .build()))
                        .build(),
                ProductDto.builder()
                        .name("testowy produkt 3")
                        .id(UUID.fromString("37abf227-91a4-4ee2-a5a8-c74a87c7db26"))
                        .kcal(14.0)
                        .type(ProductType.MEAT_AND_FISH)
                        .parameters(List.of(ProductParameterDto.builder()
                                .id(UUID.fromString("37abf227-91a4-4ee2-a5a8-c74a87c7db29"))
                                .name("parametr 3")
                                .value("wartość 3")
                                .build()))
                        .build());
    }

    public static List<RecipeDto> createRecipes() {
        return List.of(RecipeDto.builder()
                        .products(List.of(RecipeProductEntryDto.builder()
                                .amountInGrams(200.0)
                                .product(ProductDto.builder()
                                        .name("testowy produkt 1")
                                        .id(UUID.fromString("37abf227-91a4-4ee2-a5a8-c74a87c7db24"))
                                        .kcal(12.123)
                                        .type(ProductType.FATS)
                                        .parameters(List.of(ProductParameterDto.builder()
                                                .id(UUID.fromString("37abf227-91a4-4ee2-a5a8-c74a87c7db27"))
                                                .name("parametr 1")
                                                .value("wartość 1")
                                                .build()))
                                        .build())
                                .id(UUID.fromString("0bb6a36e-011f-4251-8b0b-cc9ceb915a9e"))
                                .build()))
                        .description("Opis 1")
                        .id(UUID.fromString("0bb5a36e-011f-4251-8b0b-cc9ceb915a9e"))
                        .type(RecipeType.BREAKFAST)
                        .name("Przepis 1")
                        .build(),
                RecipeDto.builder()
                        .products(List.of(RecipeProductEntryDto.builder()
                                .amountInGrams(300.0)
                                .product(ProductDto.builder()
                                        .name("testowy produkt 2")
                                        .id(UUID.fromString("37abf227-91a4-4ee2-a5a8-c74a87c7db25"))
                                        .kcal(15.123)
                                        .type(ProductType.GRAINS)
                                        .parameters(List.of(ProductParameterDto.builder()
                                                .id(UUID.fromString("37abf227-91a4-4ee2-a5a8-c74a87c7db28"))
                                                .name("parametr 2")
                                                .value("wartość 2")
                                                .build()))
                                        .build())
                                .id(UUID.fromString("0bb7a36e-011f-4251-8b0b-cc9ceb915a9e"))
                                .build()))
                        .description("Opis 2")
                        .id(UUID.fromString("0bb6a36e-011f-4251-8b0b-cc9ceb915a9e"))
                        .type(RecipeType.TEA)
                        .name("Przepis 2")
                        .build());
    }

    public static List<ShoppingListDto> createShoppingLists() {
        return List.of(ShoppingListDto.builder()
                .id(UUID.fromString("1ddfd3e4-ec0e-4179-bc9c-831cb89131b7"))
                .name("Lista 1")
                .recipes(List.of(RecipeDto.builder()
                        .products(List.of(RecipeProductEntryDto.builder()
                                .amountInGrams(200.0)
                                .product(ProductDto.builder()
                                        .name("testowy produkt 1")
                                        .id(UUID.fromString("37abf227-91a4-4ee2-a5a8-c74a87c7db24"))
                                        .kcal(12.123)
                                        .type(ProductType.FATS)
                                        .parameters(List.of(ProductParameterDto.builder()
                                                .id(UUID.fromString("37abf227-91a4-4ee2-a5a8-c74a87c7db27"))
                                                .name("parametr 1")
                                                .value("wartość 1")
                                                .build()))
                                        .build())
                                .id(UUID.fromString("0bb6a36e-011f-4251-8b0b-cc9ceb915a9e"))
                                .build()))
                        .description("Opis 1")
                        .id(UUID.fromString("0bb5a36e-011f-4251-8b0b-cc9ceb915a9e"))
                        .type(RecipeType.BREAKFAST)
                        .name("Przepis 1")
                        .build()))
                .products(List.of(ShoppingListProductEntryDto.builder()
                        .id(UUID.fromString("37abf227-91a4-4ee2-a5a8-c74a87c7db35"))
                        .amountInGrams(100.0)
                        .product(ProductDto.builder()
                                .name("testowy produkt 1")
                                .id(UUID.fromString("37abf227-91a4-4ee2-a5a8-c74a87c7db24"))
                                .kcal(12.123)
                                .type(ProductType.FATS)
                                .parameters(List.of(ProductParameterDto.builder()
                                        .id(UUID.fromString("37abf227-91a4-4ee2-a5a8-c74a87c7db27"))
                                        .name("parametr 1")
                                        .value("wartość 1")
                                        .build()))
                                .build())
                        .build()))
                .build());
    }

    public static List<BmiDto> createBmiEntries() {
        return List.of(BmiDto.builder()
                        .id(UUID.fromString("a4918a8e-cbc8-4e9e-bdef-13ed59b2be23"))
                        .height(100.0)
                        .unit(UnitSystem.IMPERIAL)
                        .weight(200.0)
                        .addedDate(LocalDate.of(2024, 3, 20))
                        .rate(BmiRate.CORRECT_VALUE)
                        .value(23.0)
                        .build(),
                BmiDto.builder()
                        .id(UUID.fromString("a4918a8e-cbc8-4e9e-bdef-13ed59b2be24"))
                        .height(200.0)
                        .unit(UnitSystem.METRIC)
                        .weight(200.0)
                        .addedDate(LocalDate.of(2024, 3, 20))
                        .rate(BmiRate.CORRECT_VALUE)
                        .value(23.0)
                        .build(),
                BmiDto.builder()
                        .id(UUID.fromString("a4918a8e-cbc8-4e9e-bdef-13ed59b2be25"))
                        .height(300.0)
                        .unit(UnitSystem.METRIC)
                        .weight(200.0)
                        .addedDate(LocalDate.of(2024, 3, 20))
                        .rate(BmiRate.CORRECT_VALUE)
                        .value(23.0)
                        .build());
    }

    public static List<TotalCaloricNeedsDto> createCaloricNeedsEntries() {
        return List.of(TotalCaloricNeedsDto.builder()
                .id(UUID.fromString("a4918a8e-cbc8-4e9e-bdef-13ed59b2be25"))
                .height(300.0)
                .weight(200.0)
                .addedDate(LocalDate.of(2024, 3, 20))
                .value(2000.0)
                .unit(UnitSystem.METRIC)
                .gender(Gender.FEMALE)
                .activityLevel(ActivityLevel.BIG)
                .age(25)
                .build());
    }
}

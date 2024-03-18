package pl.daveproject.webdiet.shoppinglist.ui;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin.Top;
import org.apache.commons.lang3.StringUtils;
import pl.daveproject.webdiet.recipe.model.RecipeDto;
import pl.daveproject.webdiet.recipe.ui.RecipeDetailsDialog;
import pl.daveproject.webdiet.shoppinglist.model.ShoppingListDto;
import pl.daveproject.webdiet.shoppinglist.model.productentry.ShoppingListProductEntryDto;
import pl.daveproject.webdiet.ui.component.CloseableDialog;
import pl.daveproject.webdiet.ui.component.Translator;
import pl.daveproject.webdiet.ui.component.ViewDetailsButton;

public class ShoppingListDetailsDialog extends CloseableDialog implements Translator {

    public ShoppingListDetailsDialog(ShoppingListDto shoppingListDto) {
        super("%s (%s kcal)".formatted(shoppingListDto.getName(), shoppingListDto.getKcal()), false);

        add(getProductEntryGrid(shoppingListDto));
        add(getRecipesGrid(shoppingListDto));
    }

    private Grid<ShoppingListProductEntryDto> getProductEntryGrid(ShoppingListDto shoppingListUiModel) {
        var shoppingListProductEntryGrid = new Grid<ShoppingListProductEntryDto>();
        shoppingListProductEntryGrid.addClassNames(Top.MEDIUM);
        shoppingListProductEntryGrid.setItems(shoppingListUiModel.getProducts());
        shoppingListProductEntryGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

        shoppingListProductEntryGrid.addColumn(
                        recipeProductEntry -> recipeProductEntry.getProduct().getName())
                .setHeader(getTranslation("products-page.grid-label-name"))
                .setSortable(true)
                .setResizable(true);

        shoppingListProductEntryGrid.addColumn(ShoppingListProductEntryDto::getRoundedAmountInGrams)
                .setHeader(getTranslation("recipe-form-amount-in-grams"))
                .setSortable(true)
                .setResizable(true);
        return shoppingListProductEntryGrid;
    }

    private Grid<RecipeDto> getRecipesGrid(ShoppingListDto shoppingListDto) {
        var recipeGrid = new Grid<RecipeDto>();
        recipeGrid.setItems(shoppingListDto.getRecipes());
        recipeGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
        recipeGrid.addClassNames(Top.MEDIUM);

        recipeGrid.addColumn(RecipeDto::getName)
                .setHeader(getTranslation("recipes-page.grid-label-name"))
                .setSortable(true)
                .setResizable(true);

        recipeGrid.addColumn(RecipeDto::getRoundedKcal)
                .setHeader(getTranslation("recipes-page.grid-label-kcal"))
                .setSortable(true)
                .setResizable(true);

        recipeGrid.addColumn(recipe -> getTranslation(recipe.getType().getTranslationKey()))
                .setHeader(getTranslation("recipes-page.grid-label-type"))
                .setSortable(true)
                .setResizable(true);

        recipeGrid.addColumn(new ComponentRenderer<>(recipe -> {
                    var viewDetailsButton = new ViewDetailsButton();
                    viewDetailsButton.addClickListener(e -> {
                        var recipeDetailsDialog = new RecipeDetailsDialog(recipe);
                        add(recipeDetailsDialog);
                        recipeDetailsDialog.open();
                    });
                    return viewDetailsButton;
                }))
                .setHeader(StringUtils.EMPTY)
                .setSortable(false)
                .setResizable(false);
        return recipeGrid;
    }
}

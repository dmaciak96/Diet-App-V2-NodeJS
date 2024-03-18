package pl.daveproject.webdiet.recipe.ui;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin.Top;
import lombok.extern.slf4j.Slf4j;
import pl.daveproject.webdiet.recipe.model.RecipeDto;
import pl.daveproject.webdiet.recipe.model.productentry.RecipeProductEntryDto;
import pl.daveproject.webdiet.ui.component.CloseableDialog;
import pl.daveproject.webdiet.ui.component.Translator;

@Slf4j
public class RecipeDetailsDialog extends CloseableDialog implements Translator {

    public RecipeDetailsDialog(RecipeDto recipeUiModel) {
        super("%s (%s kcal)".formatted(recipeUiModel.getName(), recipeUiModel.getRoundedKcal()), false);
        add(new Text(recipeUiModel.getDescription()));
        add(getRecipeProductEntryGrid(recipeUiModel));
    }

    private Grid<RecipeProductEntryDto> getRecipeProductEntryGrid(RecipeDto recipeUiModel) {
        var recipeProductEntryGrid = new Grid<RecipeProductEntryDto>();
        recipeProductEntryGrid.addClassNames(Top.MEDIUM);
        recipeProductEntryGrid.setItems(recipeUiModel.getProducts());
        recipeProductEntryGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

        recipeProductEntryGrid.addColumn(recipeProductEntry -> recipeProductEntry.getProduct().getName())
                .setHeader(getTranslation("products-page.grid-label-name"))
                .setSortable(true)
                .setResizable(true);

        recipeProductEntryGrid.addColumn(RecipeProductEntryDto::getRoundedAmountInGrams)
                .setHeader(getTranslation("recipe-form-amount-in-grams"))
                .setSortable(true)
                .setResizable(true);
        return recipeProductEntryGrid;
    }
}

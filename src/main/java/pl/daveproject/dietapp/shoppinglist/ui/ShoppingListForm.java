package pl.daveproject.dietapp.shoppinglist.ui;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin.Top;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import pl.daveproject.dietapp.recipe.model.RecipeDto;
import pl.daveproject.dietapp.recipe.ui.RecipeDataProvider;
import pl.daveproject.dietapp.recipe.ui.RecipeDetailsDialog;
import pl.daveproject.dietapp.shoppinglist.model.ShoppingListRequest;
import pl.daveproject.dietapp.shoppinglist.service.ShoppingListService;
import pl.daveproject.dietapp.ui.component.ViewDetailsButton;

public class ShoppingListForm extends FormLayout {

    private final Binder<ShoppingListRequest> binder;
    private final TextField name;
    private final Grid<RecipeDto> recipeGrid;
    private final RecipeDataProvider recipeDataProvider;
    private final ShoppingListService shoppingListService;

    public ShoppingListForm(ShoppingListRequest shoppingListRequest,
                            RecipeDataProvider recipeDataProvider, ShoppingListService shoppingListService) {
        this.name = new TextField(getTranslation("shopping-lists-page.grid-label-name"));
        this.recipeGrid = new Grid<>();
        this.recipeDataProvider = recipeDataProvider;
        this.shoppingListService = shoppingListService;

        this.binder = new BeanValidationBinder<>(ShoppingListRequest.class);
        this.binder.setBean(shoppingListRequest);
        this.binder.bindInstanceFields(this);
        setupGrid();

        add(name, 2);
        add(recipeGrid, 2);

        createSubmitButtons();
    }

    private void setupGrid() {
        recipeGrid.setItems(recipeDataProvider.withConfigurableFilter());
        recipeGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
        recipeGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        recipeGrid.addClassNames(Top.MEDIUM);
        recipeGrid.addSelectionListener(selectedRecipes ->
                binder.getBean().setRecipes(selectedRecipes.getAllSelectedItems().stream().map(RecipeDto::getId).toList()));

        recipeGrid.addColumn(RecipeDto::getName, RecipeDataProvider.NAME_SORTING_KEY)
                .setHeader(getTranslation("recipes-page.grid-label-name"))
                .setSortable(true)
                .setResizable(true);

        recipeGrid.addColumn(RecipeDto::getRoundedKcal,
                        RecipeDataProvider.KCAL_SORTING_KEY)
                .setHeader(getTranslation("recipes-page.grid-label-kcal"))
                .setSortable(true)
                .setResizable(true);

        recipeGrid.addColumn(recipe -> getTranslation(recipe.getType().getTranslationKey()),
                        RecipeDataProvider.TYPE_SORTING_KEY)
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
    }

    private void createSubmitButtons() {
        var save = new Button(getTranslation("form.save"));
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickShortcut(Key.ENTER);
        save.addClickListener(event -> validateAndSave());

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        var buttonLayout = new HorizontalLayout(save);
        buttonLayout.addClassNames(LumoUtility.Margin.Top.MEDIUM);
        add(buttonLayout);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(
                    new ShoppingListForm.SaveEvent(this, binder.getBean()));
        }
    }

    @Getter
    public static abstract class ShoppingListFormEvent extends ComponentEvent<ShoppingListForm> {

        private final ShoppingListRequest shoppingListRequest;

        protected ShoppingListFormEvent(ShoppingListForm source, ShoppingListRequest shoppingListRequest) {
            super(source, false);
            this.shoppingListRequest = shoppingListRequest;
        }
    }

    public static class SaveEvent extends ShoppingListForm.ShoppingListFormEvent {

        SaveEvent(ShoppingListForm source, ShoppingListRequest shoppingList) {
            super(source, shoppingList);
        }
    }

    public Registration addSaveListener(ComponentEventListener<ShoppingListForm.SaveEvent> listener) {
        return addListener(ShoppingListForm.SaveEvent.class, listener);
    }
}

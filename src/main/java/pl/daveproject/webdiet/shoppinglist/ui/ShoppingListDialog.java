package pl.daveproject.webdiet.shoppinglist.ui;

import lombok.extern.slf4j.Slf4j;
import pl.daveproject.webdiet.recipe.ui.RecipeDataProvider;
import pl.daveproject.webdiet.shoppinglist.model.ShoppingListRequest;
import pl.daveproject.webdiet.shoppinglist.service.ShoppingListService;
import pl.daveproject.webdiet.ui.component.CloseableDialog;
import pl.daveproject.webdiet.ui.component.Translator;
import pl.daveproject.webdiet.ui.component.WebdietFormWrapper;


@Slf4j
public class ShoppingListDialog extends CloseableDialog implements Translator {

    private final WebdietFormWrapper shoppingListForm;
    private final ShoppingListService shoppingListService;

    public ShoppingListDialog(ShoppingListService shoppingListService,
                              ShoppingListRequest shoppingListRequest, RecipeDataProvider recipeDataProvider) {
        super("shopping-lists-form.title");
        this.shoppingListForm = new WebdietFormWrapper(
                new ShoppingListForm(shoppingListRequest, recipeDataProvider, shoppingListService),
                false);
        this.shoppingListService = shoppingListService;
        add(shoppingListForm);
        saveShoppingListOnSave();
    }

    private void saveShoppingListOnSave() {
        var form = (ShoppingListForm) shoppingListForm.getFormLayout();
        form.addSaveListener(e -> {
            log.info("Creating/Updating shopping list: {}", e.getShoppingListRequest().getName());
            var createdShoppingList = shoppingListService.save(e.getShoppingListRequest());
            log.info("Shopping list {} successfully created/updated", createdShoppingList.getName());
            this.close();
        });
    }
}

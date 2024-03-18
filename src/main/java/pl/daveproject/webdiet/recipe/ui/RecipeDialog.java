package pl.daveproject.webdiet.recipe.ui;

import lombok.extern.slf4j.Slf4j;
import pl.daveproject.webdiet.product.mapper.ProductMapper;
import pl.daveproject.webdiet.product.service.ProductService;
import pl.daveproject.webdiet.product.ui.ProductDialog;
import pl.daveproject.webdiet.recipe.model.RecipeRequest;
import pl.daveproject.webdiet.recipe.service.RecipeService;
import pl.daveproject.webdiet.ui.component.CloseableDialog;
import pl.daveproject.webdiet.ui.component.Translator;
import pl.daveproject.webdiet.ui.component.WebdietFormWrapper;

@Slf4j
public class RecipeDialog extends CloseableDialog implements Translator {

    private final WebdietFormWrapper recipeForm;
    private final RecipeService recipeService;
    private final ProductService productService;
    private final ProductMapper productMapper;

    public RecipeDialog(RecipeService recipeService,
                        RecipeRequest recipeRequest,
                        ProductService productService,
                        ProductMapper productMapper) {
        super("recipes-window.title");
        this.recipeService = recipeService;
        this.productMapper = productMapper;
        this.productService = productService;
        this.recipeForm = new WebdietFormWrapper(new RecipeForm(recipeRequest, productService), false);
        add(recipeForm);
        saveOrUpdateRecipeOnSave();
    }

    private void saveOrUpdateRecipeOnSave() {
        var form = (RecipeForm) recipeForm.getFormLayout();
        form.addSaveListener(e -> {
            if (recipeService.isAllProductExists(e.getRecipeRequest().getProducts())) {
                if (e.getRecipeRequest().getId() == null) {
                    log.debug("Creating recipe: {}", e.getRecipeRequest().getName());
                    var createdRecipe = recipeService.save(e.getRecipeRequest());
                    log.info("Recipe {} successfully created", createdRecipe.getName());
                } else {
                    log.debug("Updating recipe: {}", e.getRecipeRequest().getName());
                    var createdRecipe = recipeService.update(e.getRecipeRequest().getId(), e.getRecipeRequest());
                    log.info("Recipe {} successfully updated", createdRecipe.getName());
                }
                this.close();
            } else {
                var notExistingProducts = recipeService.getNotExistingProducts(e.getRecipeRequest().getProducts());
                for (var product : notExistingProducts) {
                    var productDialog = new ProductDialog(productService, productMapper.dtoToRequest(product), productMapper);
                    add(productDialog);
                    productDialog.open();
                }
            }
        });
    }
}

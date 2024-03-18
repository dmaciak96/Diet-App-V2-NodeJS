package pl.daveproject.dietapp.recipe.ui;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.apache.commons.lang3.StringUtils;
import pl.daveproject.dietapp.product.mapper.ProductMapper;
import pl.daveproject.dietapp.product.service.ProductService;
import pl.daveproject.dietapp.recipe.mapper.RecipeMapper;
import pl.daveproject.dietapp.recipe.model.RecipeDto;
import pl.daveproject.dietapp.recipe.model.productentry.RecipeProductEntryRequest;
import pl.daveproject.dietapp.recipe.service.RecipeService;
import pl.daveproject.dietapp.ui.component.DeleteConfirmDialog;
import pl.daveproject.dietapp.ui.component.ViewDetailsButton;
import pl.daveproject.dietapp.ui.component.grid.CrudGrid;
import pl.daveproject.dietapp.ui.layout.AfterLoginAppLayout;

import java.util.List;

@PermitAll
@Route(value = "/recipes", layout = AfterLoginAppLayout.class)
public class RecipeView extends VerticalLayout implements HasDynamicTitle {

    private final RecipeService recipeService;
    private final ProductService productService;
    private final CrudGrid<RecipeDto, RecipeFilter> recipeGrid;
    private final RecipeMapper recipeMapper;
    private final ProductMapper productMapper;

    public RecipeView(RecipeService recipeService,
                      ProductService productService, RecipeFilter recipeFilter,
                      RecipeDataProvider recipeDataProvider,
                      RecipeMapper recipeMapper,
                      ProductMapper productMapper) {
        this.productService = productService;
        this.recipeMapper = recipeMapper;
        this.productMapper = productMapper;
        this.recipeGrid = new CrudGrid<>(recipeDataProvider, recipeFilter);
        this.recipeService = recipeService;

        createGridColumns();
        setOnNewClickListener();
        setOnEditClickListener();
        setOnDeleteClickListener();
        add(recipeGrid);
    }

    private void createGridColumns() {
        recipeGrid.getGrid().addColumn(RecipeDto::getName, RecipeDataProvider.NAME_SORTING_KEY)
                .setHeader(getTranslation("recipes-page.grid-label-name"))
                .setSortable(true)
                .setResizable(true);

        recipeGrid.getGrid().addColumn(RecipeDto::getRoundedKcal,
                        RecipeDataProvider.KCAL_SORTING_KEY)
                .setHeader(getTranslation("recipes-page.grid-label-kcal"))
                .setSortable(true)
                .setResizable(true);

        recipeGrid.getGrid().addColumn(recipe -> getTranslation(recipe.getType().getTranslationKey()),
                        RecipeDataProvider.TYPE_SORTING_KEY)
                .setHeader(getTranslation("recipes-page.grid-label-type"))
                .setSortable(true)
                .setResizable(true);

        recipeGrid.getGrid().addColumn(new ComponentRenderer<>(recipe -> {
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

    private void setOnNewClickListener() {
        recipeGrid.addOnClickListener(event ->
                createAndOpenRecipeDialog(RecipeDto.builder()
                        .products(List.of())
                        .build()));
    }

    private void setOnEditClickListener() {
        recipeGrid.editOnClickListener(event -> {
            var selectedProduct = recipeGrid.getGrid()
                    .getSelectedItems()
                    .stream()
                    .findFirst();
            selectedProduct.ifPresent(this::createAndOpenRecipeDialog);
        });
    }

    private void createAndOpenRecipeDialog(RecipeDto recipeDto) {
        var request = recipeMapper.dtoToRequest(recipeDto);
        request.setProducts(recipeDto.getProducts().stream()
                .map(e -> RecipeProductEntryRequest.builder()
                        .productName(e.getProduct().getName())
                        .productId(e.getProduct().getId())
                        .amountInGrams(e.getAmountInGrams())
                        .build())
                .toList());
        var recipeDialog = new RecipeDialog(recipeService, request, productService, productMapper);
        add(recipeDialog);
        recipeDialog.open();
        recipeDialog.addOpenedChangeListener(e -> {
            if (!e.isOpened()) {
                recipeGrid.refresh();
            }
        });
    }

    private void setOnDeleteClickListener() {
        recipeGrid.deleteOnClickListener(event -> {
            var selectedProduct = recipeGrid.getGrid()
                    .getSelectedItems()
                    .stream()
                    .findFirst();
            selectedProduct.ifPresent(this::createAndOpenDeleteDialog);
        });
    }

    private void createAndOpenDeleteDialog(RecipeDto recipeDto) {
        var confirmDialogSuffix = "%s \"%s\"".formatted(
                getTranslation("delete-dialog.header-recipe-suffix"),
                recipeDto.getName());
        var confirmDialog = new DeleteConfirmDialog(confirmDialogSuffix);
        confirmDialog.open();
        confirmDialog.addConfirmListener(event -> {
            recipeService.delete(recipeDto);
            recipeGrid.refresh();
            confirmDialog.close();
        });
    }

    @Override
    public String getPageTitle() {
        return getTranslation("recipes-page.title");
    }
}

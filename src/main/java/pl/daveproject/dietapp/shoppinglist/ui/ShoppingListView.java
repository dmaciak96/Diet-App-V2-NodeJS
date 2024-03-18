package pl.daveproject.dietapp.shoppinglist.ui;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.apache.commons.lang3.StringUtils;
import pl.daveproject.dietapp.recipe.model.RecipeDto;
import pl.daveproject.dietapp.recipe.ui.RecipeDataProvider;
import pl.daveproject.dietapp.shoppinglist.mapper.ShoppingListMapper;
import pl.daveproject.dietapp.shoppinglist.model.ShoppingListDto;
import pl.daveproject.dietapp.shoppinglist.model.ShoppingListRequest;
import pl.daveproject.dietapp.shoppinglist.service.ShoppingListService;
import pl.daveproject.dietapp.ui.component.DeleteConfirmDialog;
import pl.daveproject.dietapp.ui.component.ViewDetailsButton;
import pl.daveproject.dietapp.ui.component.grid.CrudGrid;
import pl.daveproject.dietapp.ui.layout.AfterLoginAppLayout;

import java.util.List;

@PermitAll
@Route(value = "/shopping-lists", layout = AfterLoginAppLayout.class)
public class ShoppingListView extends VerticalLayout implements HasDynamicTitle {

    private final CrudGrid<ShoppingListDto, ShoppingListFilter> shoppingListGrid;
    private final ShoppingListService shoppingListService;
    private final RecipeDataProvider recipeDataProvider;
    private final ShoppingListMapper shoppingListMapper;

    public ShoppingListView(ShoppingListService shoppingListService,
                            ShoppingListDataProvider shoppingListDataProvider,
                            ShoppingListFilter shoppingListFilter,
                            RecipeDataProvider recipeDataProvider,
                            ShoppingListMapper shoppingListMapper) {
        this.shoppingListService = shoppingListService;
        this.recipeDataProvider = recipeDataProvider;
        this.shoppingListGrid = new CrudGrid<>(shoppingListDataProvider, shoppingListFilter, false);
        this.shoppingListMapper = shoppingListMapper;

        createGridColumns();
        setOnNewClickListener();
        setOnEditClickListener();
        setOnDeleteClickListener();
        add(shoppingListGrid);
    }

    private void createGridColumns() {
        shoppingListGrid.getGrid()
                .addColumn(ShoppingListDto::getName, ShoppingListDataProvider.NAME_SORTING_KEY)
                .setHeader(getTranslation("shopping-lists-page.grid-label-name"))
                .setSortable(true)
                .setResizable(true);

        shoppingListGrid.getGrid().addColumn(new ComponentRenderer<>(shoppingListUiModel -> {
                    var viewDetailsButton = new ViewDetailsButton();
                    viewDetailsButton.addClickListener(e -> {
                        var shoppingListDetailsDialog = new ShoppingListDetailsDialog(shoppingListUiModel);
                        add(shoppingListDetailsDialog);
                        shoppingListDetailsDialog.open();
                    });
                    return viewDetailsButton;
                }))
                .setHeader(StringUtils.EMPTY)
                .setSortable(false)
                .setResizable(false);
    }

    private void setOnNewClickListener() {
        shoppingListGrid.addOnClickListener(event ->
                createAndOpenShoppingListDialog(ShoppingListRequest.builder()
                        .recipes(List.of())
                        .build()));
    }

    private void setOnEditClickListener() {
        shoppingListGrid.editOnClickListener(event -> {
            var selectedProduct = shoppingListGrid.getGrid()
                    .getSelectedItems()
                    .stream()
                    .findFirst();
            selectedProduct.ifPresent(
                    e -> {
                        var recipeIds = e.getRecipes().stream()
                                .map(RecipeDto::getId)
                                .toList();
                        createAndOpenShoppingListDialog(ShoppingListRequest.builder()
                                .id(e.getId())
                                .recipes(recipeIds)
                                .name(e.getName())
                                .build());
                    });
        });
    }

    private void createAndOpenShoppingListDialog(ShoppingListRequest shoppingListRequest) {
        var shoppingListDialog = new ShoppingListDialog(shoppingListService, shoppingListRequest, recipeDataProvider);
        add(shoppingListDialog);
        shoppingListDialog.open();
        shoppingListDialog.addOpenedChangeListener(e -> {
            if (!e.isOpened()) {
                shoppingListGrid.refresh();
            }
        });
    }

    private void setOnDeleteClickListener() {
        shoppingListGrid.deleteOnClickListener(event -> {
            var selected = shoppingListGrid.getGrid()
                    .getSelectedItems()
                    .stream()
                    .findFirst();
            selected.ifPresent(this::createAndOpenDeleteDialog);
        });
    }

    private void createAndOpenDeleteDialog(ShoppingListDto shoppingListDto) {
        var confirmDialogSuffix = "%s \"%s\"".formatted(
                getTranslation("delete-dialog.header-shopping-list-suffix"),
                shoppingListDto.getName());
        var confirmDialog = new DeleteConfirmDialog(confirmDialogSuffix);
        confirmDialog.open();
        confirmDialog.addConfirmListener(event -> {
            shoppingListService.delete(shoppingListDto);
            shoppingListGrid.refresh();
            confirmDialog.close();
        });
    }

    @Override
    public String getPageTitle() {
        return getTranslation("shopping-lists-page.title");
    }
}

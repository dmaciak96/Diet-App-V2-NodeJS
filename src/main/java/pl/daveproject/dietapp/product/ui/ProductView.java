package pl.daveproject.dietapp.product.ui;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import pl.daveproject.dietapp.product.mapper.ProductMapper;
import pl.daveproject.dietapp.product.model.ProductDto;
import pl.daveproject.dietapp.product.model.ProductRequest;
import pl.daveproject.dietapp.product.service.ProductService;
import pl.daveproject.dietapp.ui.component.DeleteConfirmDialog;
import pl.daveproject.dietapp.ui.component.grid.CrudGrid;
import pl.daveproject.dietapp.ui.layout.AfterLoginAppLayout;

@PermitAll
@Route(value = "/products", layout = AfterLoginAppLayout.class)
public class ProductView extends VerticalLayout implements HasDynamicTitle {

    private final CrudGrid<ProductDto, ProductFilter> productGrid;
    private final ProductService productService;
    private final ProductMapper productMapper;

    public ProductView(ProductFilter productFilter,
                       ProductDataProvider productDataProvider,
                       ProductService productService,
                       ProductMapper productMapper) {
        this.productMapper = productMapper;
        this.productGrid = new CrudGrid<>(productDataProvider, productFilter);
        this.productService = productService;
        createGridColumns();
        setOnNewClickListener();
        setOnEditClickListener();
        setOnDeleteClickListener();
        add(productGrid);
    }

    private void createGridColumns() {
        productGrid.getGrid().addColumn(ProductDto::getName, ProductDataProvider.NAME_SORTING_KEY)
                .setHeader(getTranslation("products-page.grid-label-name"))
                .setSortable(true)
                .setResizable(true);

        productGrid.getGrid().addColumn(product -> (double) Math.round(product.getKcal() * 100) / 100, ProductDataProvider.KCAL_SORTING_KEY)
                .setHeader(getTranslation("products-page.grid-label-kcal"))
                .setSortable(true)
                .setResizable(true);

        productGrid.getGrid().addColumn(product -> getTranslation(product.getType().getTranslationKey()), ProductDataProvider.TYPE_SORTING_KEY)
                .setHeader(getTranslation("products-page.grid-label-type"))
                .setSortable(true)
                .setResizable(true);
    }

    private void setOnNewClickListener() {
        productGrid.addOnClickListener(event ->
                createAndOpenProductDialog(ProductRequest.builder().build()));
    }

    private void setOnEditClickListener() {
        productGrid.editOnClickListener(event -> {
            var selectedProduct = productGrid.getGrid()
                    .getSelectedItems()
                    .stream()
                    .findFirst();
            selectedProduct.ifPresent(e -> createAndOpenProductDialog(productMapper.dtoToRequest(e)));
        });
    }

    private void createAndOpenProductDialog(ProductRequest product) {
        var productDialog = new ProductDialog(productService, product, productMapper);
        add(productDialog);
        productDialog.open();
        productDialog.addOpenedChangeListener(e -> {
            if (!e.isOpened()) {
                productGrid.refresh();
            }
        });
    }

    private void setOnDeleteClickListener() {
        productGrid.deleteOnClickListener(event -> {
            var selectedProduct = productGrid.getGrid()
                    .getSelectedItems()
                    .stream()
                    .findFirst();
            selectedProduct.ifPresent(this::createAndOpenProductDeleteDialog);
        });
    }

    private void createAndOpenProductDeleteDialog(ProductDto product) {
        var confirmDialogSuffix = "%s \"%s\"".formatted(getTranslation("delete-dialog.header-product-suffix"),
                product.getName());
        var confirmDialog = new DeleteConfirmDialog(confirmDialogSuffix);
        confirmDialog.open();
        confirmDialog.addConfirmListener(event -> {
            productService.delete(product);
            productGrid.refresh();
            confirmDialog.close();
        });
    }

    @Override
    public String getPageTitle() {
        return getTranslation("products-page.title");
    }
}

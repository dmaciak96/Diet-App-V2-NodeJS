package pl.daveproject.dietapp.product.ui;

import lombok.extern.slf4j.Slf4j;
import pl.daveproject.dietapp.product.mapper.ProductMapper;
import pl.daveproject.dietapp.product.model.ProductRequest;
import pl.daveproject.dietapp.product.service.ProductService;
import pl.daveproject.dietapp.ui.component.CloseableDialog;
import pl.daveproject.dietapp.ui.component.Translator;
import pl.daveproject.dietapp.ui.component.WebdietFormWrapper;

import java.util.List;

@Slf4j
public class ProductDialog extends CloseableDialog implements Translator {

    private final WebdietFormWrapper productForm;
    private final ProductService productService;
    private final ProductMapper productMapper;

    public ProductDialog(ProductService productService, ProductRequest product, ProductMapper productMapper) {
        super("products-window.title");
        this.productService = productService;
        this.productMapper = productMapper;
        this.productForm = new WebdietFormWrapper(new ProductForm(product), false);
        add(productForm);
        saveOrUpdateProductOnSave();
    }

    private void saveOrUpdateProductOnSave() {
        var form = (ProductForm) productForm.getFormLayout();
        form.addSaveListener(e -> {
            var dto = productMapper.requestToDto(e.getProductRequest());
            if(dto.getParameters() == null) {
                dto.setParameters(List.of());
            }
            if (dto.getId() == null) {
                log.debug("Creating product: {}", dto.getName());
                var createdProduct = productService.save(dto);
                log.info("Product {} successfully created", createdProduct.getName());
            } else {
                log.debug("Updating product: {}", dto.getName());
                var createdProduct = productService.update(dto.getId(), dto);
                log.info("Product {} successfully updated", createdProduct.getName());
            }
            this.close();
        });
    }
}

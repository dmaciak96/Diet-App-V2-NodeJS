package pl.daveproject.dietapp.product.ui;

import lombok.Setter;
import org.springframework.stereotype.Component;
import pl.daveproject.dietapp.product.model.ProductDto;
import pl.daveproject.dietapp.ui.component.Translator;
import pl.daveproject.dietapp.ui.component.grid.GridDataFilter;

@Setter
@Component
public class ProductFilter implements Translator, GridDataFilter {
    private String searchValue;

    public boolean match(ProductDto product) {
        var matchesProductName = matches(product.getName(), searchValue);
        var matchesProductType = matches(getTranslation(product.getType().getTranslationKey()), searchValue);
        return matchesProductName || matchesProductType;
    }

    private boolean matches(String value, String searchTerm) {
        return searchTerm == null || searchTerm.isEmpty()
                || value.toLowerCase().contains(searchTerm.toLowerCase());
    }
}

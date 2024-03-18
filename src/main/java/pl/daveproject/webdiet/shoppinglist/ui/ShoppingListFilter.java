package pl.daveproject.webdiet.shoppinglist.ui;

import lombok.Setter;
import org.springframework.stereotype.Component;
import pl.daveproject.webdiet.shoppinglist.model.ShoppingListDto;
import pl.daveproject.webdiet.ui.component.Translator;
import pl.daveproject.webdiet.ui.component.grid.GridDataFilter;

@Setter
@Component
public class ShoppingListFilter implements Translator, GridDataFilter {

    private String searchValue;

    public boolean match(ShoppingListDto shoppingListUiModel) {
        return matches(shoppingListUiModel.getName(), searchValue);
    }

    private boolean matches(String value, String searchTerm) {
        return searchTerm == null || searchTerm.isEmpty()
                || value.toLowerCase().contains(searchTerm.toLowerCase());
    }

}

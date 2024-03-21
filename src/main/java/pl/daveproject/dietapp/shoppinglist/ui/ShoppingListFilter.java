package pl.daveproject.dietapp.shoppinglist.ui;

import lombok.Setter;
import org.springframework.stereotype.Component;
import pl.daveproject.dietapp.shoppinglist.model.ShoppingListDto;
import pl.daveproject.dietapp.ui.component.Translator;
import pl.daveproject.dietapp.ui.component.grid.GridDataFilter;

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

package pl.daveproject.dietapp.recipe.ui;

import lombok.Setter;
import org.springframework.stereotype.Component;
import pl.daveproject.dietapp.recipe.model.RecipeDto;
import pl.daveproject.dietapp.ui.component.Translator;
import pl.daveproject.dietapp.ui.component.grid.GridDataFilter;

@Setter
@Component
public class RecipeFilter implements Translator, GridDataFilter {

    private String searchValue;

    public boolean match(RecipeDto recipeUiModel) {
        var matchesName = matches(recipeUiModel.getName(), searchValue);
        var matchesType = matches(getTranslation(recipeUiModel.getType().getTranslationKey()), searchValue);
        return matchesName || matchesType;
    }

    private boolean matches(String value, String searchTerm) {
        return searchTerm == null || searchTerm.isEmpty()
                || value.toLowerCase().contains(searchTerm.toLowerCase());
    }
}

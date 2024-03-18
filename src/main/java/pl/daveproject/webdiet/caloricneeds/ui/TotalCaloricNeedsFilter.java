package pl.daveproject.webdiet.caloricneeds.ui;

import lombok.Setter;
import org.springframework.stereotype.Component;
import pl.daveproject.webdiet.caloricneeds.model.TotalCaloricNeedsDto;
import pl.daveproject.webdiet.ui.component.Translator;
import pl.daveproject.webdiet.ui.component.grid.GridDataFilter;

@Setter
@Component
public class TotalCaloricNeedsFilter implements Translator, GridDataFilter {

    private String searchValue;

    public boolean match(TotalCaloricNeedsDto totalCaloricNeeds) {
        return matches(getTranslation(totalCaloricNeeds.getActivityLevel().getTranslationKey()), searchValue);
    }

    private boolean matches(String value, String searchTerm) {
        return searchTerm == null || searchTerm.isEmpty()
                || value.toLowerCase().contains(searchTerm.toLowerCase());
    }
}

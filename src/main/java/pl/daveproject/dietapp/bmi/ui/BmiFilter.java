package pl.daveproject.dietapp.bmi.ui;

import lombok.Setter;
import org.springframework.stereotype.Component;
import pl.daveproject.dietapp.bmi.model.BmiDto;
import pl.daveproject.dietapp.ui.component.Translator;
import pl.daveproject.dietapp.ui.component.grid.GridDataFilter;

@Setter
@Component
public class BmiFilter implements Translator, GridDataFilter {

    private String searchValue;

    public boolean match(BmiDto bmi) {
        return matches(getTranslation(bmi.getRate().getTranslationKey()), searchValue);
    }

    private boolean matches(String value, String searchTerm) {
        return searchTerm == null || searchTerm.isEmpty()
                || value.toLowerCase().contains(searchTerm.toLowerCase());
    }
}

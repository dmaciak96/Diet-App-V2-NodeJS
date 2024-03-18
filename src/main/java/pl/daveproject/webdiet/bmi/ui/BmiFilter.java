package pl.daveproject.webdiet.bmi.ui;

import lombok.Setter;
import org.springframework.stereotype.Component;
import pl.daveproject.webdiet.bmi.model.BmiDto;
import pl.daveproject.webdiet.ui.component.Translator;
import pl.daveproject.webdiet.ui.component.grid.GridDataFilter;

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

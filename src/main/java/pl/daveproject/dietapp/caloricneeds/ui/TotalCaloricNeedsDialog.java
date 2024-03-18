package pl.daveproject.dietapp.caloricneeds.ui;

import lombok.extern.slf4j.Slf4j;
import pl.daveproject.dietapp.caloricneeds.mapper.TotalCaloricNeedsMapper;
import pl.daveproject.dietapp.caloricneeds.model.TotalCaloricNeedsRequest;
import pl.daveproject.dietapp.caloricneeds.service.TotalCaloricNeedsService;
import pl.daveproject.dietapp.ui.component.CloseableDialog;
import pl.daveproject.dietapp.ui.component.Translator;
import pl.daveproject.dietapp.ui.component.WebdietFormWrapper;

@Slf4j
public class TotalCaloricNeedsDialog extends CloseableDialog implements Translator {

    private final WebdietFormWrapper totalCaloricNeedsForm;
    private final TotalCaloricNeedsService totalCaloricNeedsService;
    private final TotalCaloricNeedsMapper totalCaloricNeedsMapper;

    public TotalCaloricNeedsDialog(TotalCaloricNeedsService totalCaloricNeedsService,
                                   TotalCaloricNeedsRequest totalCaloricNeedsRequest, TotalCaloricNeedsMapper totalCaloricNeedsMapper) {
        super("total-caloric-needs.window-title");
        this.totalCaloricNeedsService = totalCaloricNeedsService;
        this.totalCaloricNeedsForm = new WebdietFormWrapper(new TotalCaloricNeedsForm(totalCaloricNeedsRequest), false);
        this.totalCaloricNeedsMapper = totalCaloricNeedsMapper;
        add(totalCaloricNeedsForm);
        saveTotalCaloricNeedsOnSave();
    }

    private void saveTotalCaloricNeedsOnSave() {
        var form = (TotalCaloricNeedsForm) totalCaloricNeedsForm.getFormLayout();
        form.addSaveListener(e -> {
            log.debug("Creating new total caloric needs...");
            var dto = totalCaloricNeedsMapper.requestToDto(e.getTotalCaloricNeedsRequest());
            var createdBmi = totalCaloricNeedsService.save(dto);
            log.info("Total caloric needs {} successfully created", createdBmi.getId());
            this.close();
        });
    }
}

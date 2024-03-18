package pl.daveproject.webdiet.caloricneeds.ui;

import lombok.extern.slf4j.Slf4j;
import pl.daveproject.webdiet.caloricneeds.mapper.TotalCaloricNeedsMapper;
import pl.daveproject.webdiet.caloricneeds.model.TotalCaloricNeedsRequest;
import pl.daveproject.webdiet.caloricneeds.service.TotalCaloricNeedsService;
import pl.daveproject.webdiet.ui.component.CloseableDialog;
import pl.daveproject.webdiet.ui.component.Translator;
import pl.daveproject.webdiet.ui.component.WebdietFormWrapper;

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

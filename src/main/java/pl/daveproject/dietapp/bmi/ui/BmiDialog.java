package pl.daveproject.dietapp.bmi.ui;

import lombok.extern.slf4j.Slf4j;
import pl.daveproject.dietapp.bmi.mapper.BmiMapper;
import pl.daveproject.dietapp.bmi.model.BmiRequest;
import pl.daveproject.dietapp.bmi.service.BmiService;
import pl.daveproject.dietapp.ui.component.CloseableDialog;
import pl.daveproject.dietapp.ui.component.Translator;
import pl.daveproject.dietapp.ui.component.WebdietFormWrapper;

@Slf4j
public class BmiDialog extends CloseableDialog implements Translator {

    private final WebdietFormWrapper bmiForm;
    private final BmiService bmiService;
    private final BmiMapper bmiMapper;

    public BmiDialog(BmiService bmiService, BmiRequest bmi, BmiMapper bmiMapper) {
        super("bmi-view.window-title");
        this.bmiService = bmiService;
        this.bmiForm = new WebdietFormWrapper(new BmiForm(bmi), false);
        this.bmiMapper = bmiMapper;
        add(bmiForm);
        saveBmiOnSave();
    }

    private void saveBmiOnSave() {
        var form = (BmiForm) bmiForm.getFormLayout();
        form.addSaveListener(e -> {
            log.debug("Creating new BMI...");
            var bmiDto = bmiMapper.requestToDto(e.getBmiRequest());
            var createdBmi = bmiService.save(bmiDto);
            log.info("BMI {} successfully created", createdBmi.getId());
            this.close();
        });
    }
}

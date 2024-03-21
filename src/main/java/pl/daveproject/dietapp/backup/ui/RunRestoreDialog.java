package pl.daveproject.dietapp.backup.ui;

import lombok.extern.slf4j.Slf4j;
import pl.daveproject.dietapp.ui.component.CloseableDialog;
import pl.daveproject.dietapp.ui.component.Translator;

@Slf4j
public class RunRestoreDialog extends CloseableDialog implements Translator {

    public RunRestoreDialog() {
        super("backup-dialog.run-restore");
    }
}

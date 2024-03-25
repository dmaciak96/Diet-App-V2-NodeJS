package pl.daveproject.dietapp.backup.ui;

import com.vaadin.flow.component.upload.Upload;
import lombok.extern.slf4j.Slf4j;
import pl.daveproject.dietapp.backup.RestoreRunner;
import pl.daveproject.dietapp.ui.component.CloseableDialog;
import pl.daveproject.dietapp.ui.component.ProgressBar;
import pl.daveproject.dietapp.ui.component.Translator;

@Slf4j
public class RunRestoreDialog extends CloseableDialog implements Translator {

//    private final ProgressBar progressBar;
//    private final RestoreRunner restoreRunner;
//    private final Upload upload;

    public RunRestoreDialog() {
        super("backup-dialog.run-restore");
    }
}

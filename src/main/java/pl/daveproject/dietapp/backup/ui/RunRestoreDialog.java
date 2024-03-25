package pl.daveproject.dietapp.backup.ui;

import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import lombok.extern.slf4j.Slf4j;
import pl.daveproject.dietapp.backup.RestoreRunner;
import pl.daveproject.dietapp.ui.component.CloseableDialog;
import pl.daveproject.dietapp.ui.component.ProgressBar;
import pl.daveproject.dietapp.ui.component.Translator;
import pl.daveproject.dietapp.ui.component.filecomponent.ZipFileUploader;

@Slf4j
public class RunRestoreDialog extends CloseableDialog implements Translator {

    private final ProgressBar progressBar;
    private final RestoreRunner restoreRunner;
    private final ZipFileUploader zipFileUploader;

    public RunRestoreDialog(RestoreRunner restoreRunner) {
        super("backup-dialog.run-restore");
        this.restoreRunner = restoreRunner;
        this.zipFileUploader = createFileUploader();
        this.progressBar = new ProgressBar(getTranslation("restore-dialog.start-restore"));
        add(zipFileUploader);
    }

    private ZipFileUploader createFileUploader() {
        var buffer = new MemoryBuffer();
        var fileUploader = new ZipFileUploader(buffer);
        fileUploader.setWidth("100%");
        fileUploader.setMaxFiles(1);
        fileUploader.addSucceededListener(
                event -> runRestoreJob(event, buffer));
        return fileUploader;
    }

    private void runRestoreJob(SucceededEvent event, MemoryBuffer buffer) {
        add(progressBar);
        var inputStream = buffer.getInputStream();

        //todo: Read zip data here
    }
}

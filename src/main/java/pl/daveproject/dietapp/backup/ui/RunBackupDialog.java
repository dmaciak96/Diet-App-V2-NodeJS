package pl.daveproject.dietapp.backup.ui;

import lombok.extern.slf4j.Slf4j;
import pl.daveproject.dietapp.backup.model.BackupMetadataDto;
import pl.daveproject.dietapp.ui.component.CloseableDialog;
import pl.daveproject.dietapp.ui.component.Translator;

@Slf4j
public class RunBackupDialog extends CloseableDialog implements Translator {

    public RunBackupDialog(BackupMetadataDto backupMetadataDto) {
        super("backup-dialog.run-backup");
    }

    public void runBackup() {

    }
}

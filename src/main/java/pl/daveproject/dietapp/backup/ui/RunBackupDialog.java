package pl.daveproject.dietapp.backup.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import lombok.extern.slf4j.Slf4j;
import pl.daveproject.dietapp.backup.BackupRunner;
import pl.daveproject.dietapp.backup.model.BackupMetadataDto;
import pl.daveproject.dietapp.backup.model.Result;
import pl.daveproject.dietapp.backup.service.BackupService;
import pl.daveproject.dietapp.security.service.UserService;
import pl.daveproject.dietapp.ui.component.CloseableDialog;
import pl.daveproject.dietapp.ui.component.ProgressBar;
import pl.daveproject.dietapp.ui.component.Translator;

import java.util.UUID;

@Slf4j
public class RunBackupDialog extends CloseableDialog implements Translator {
    private final BackupRunner backupRunner;
    private final BackupService backupService;
    private final UserService userService;
    private final ProgressBar progressBar;

    public RunBackupDialog(BackupRunner backupRunner,
                           BackupService backupService,
                           UserService userService) {
        super("backup-dialog.run-backup-title");
        this.backupRunner = backupRunner;
        this.backupService = backupService;
        this.userService = userService;
        this.progressBar = new ProgressBar(getTranslation("backup-dialog.start-backup"));
        add(initializeRunBackupButton(), progressBar);
    }

    private Button initializeRunBackupButton() {
        var runBackupButton = new Button(getTranslation("backup-dialog.run-backup"));
        runBackupButton.setIcon(new Icon(VaadinIcon.PLAY));
        runBackupButton.addClickListener(event -> runBackup());
        return runBackupButton;
    }

    public void runBackup() {
        var currentUserEmail = userService.getCurrentUser().getEmail();
        log.info("Starting backup job for user {}", currentUserEmail);

        var backupMetadataId = backupService.save(BackupMetadataDto.builder().build()).id();
        log.debug("Saved backup metadata object {}", backupMetadataId);

        progressBar.updateLabel(getTranslation("backup-dialog.backup-products"));
        var stepResult = backupRunner.backupProducts(backupMetadataId);
        progressBar.setValue(0.2);
        if (stepResult.result() == Result.FAILED) {
            progressBar.updateLabel(getTranslation("backup-dialog.backup-products-error"));
            cleanup(backupMetadataId);
            progressBar.setErrorColor();
            return;
        }

        progressBar.updateLabel(getTranslation("backup-dialog.backup-recipes"));
        stepResult = backupRunner.backupRecipes(backupMetadataId);
        progressBar.setValue(0.4);
        if (stepResult.result() == Result.FAILED) {
            progressBar.updateLabel(getTranslation("backup-dialog.backup-recipes-error"));
            cleanup(backupMetadataId);
            progressBar.setErrorColor();
            return;
        }

        progressBar.updateLabel(getTranslation("backup-dialog.backup-shopping-lists"));
        stepResult = backupRunner.backupShoppingLists(backupMetadataId);
        progressBar.setValue(0.6);
        if (stepResult.result() == Result.FAILED) {
            progressBar.updateLabel(getTranslation("backup-dialog.backup-shopping-lists-error"));
            cleanup(backupMetadataId);
            progressBar.setErrorColor();
            return;
        }

        progressBar.updateLabel(getTranslation("backup-dialog.backup-bmi"));
        stepResult = backupRunner.backupBmi(backupMetadataId);
        progressBar.setValue(0.8);
        if (stepResult.result() == Result.FAILED) {
            progressBar.updateLabel(getTranslation("backup-dialog.backup-bmi-error"));
            cleanup(backupMetadataId);
            progressBar.setErrorColor();
            return;
        }

        progressBar.updateLabel(getTranslation("backup-dialog.backup-caloric-needs"));
        stepResult = backupRunner.backupTotalCaloricNeeds(backupMetadataId);
        progressBar.setValue(0.9);
        if (stepResult.result() == Result.FAILED) {
            progressBar.updateLabel(getTranslation("backup-dialog.backup-caloric-needs-error"));
            cleanup(backupMetadataId);
            progressBar.setErrorColor();
            return;
        }

        progressBar.updateLabel(getTranslation("backup-dialog.backup-success"));
        progressBar.setValue(1.0);
        progressBar.setSuccessColor();
        log.info("Backup ends successfully for user {}", currentUserEmail);
    }

    private void cleanup(UUID backupMetadataId) {
        try {
            log.info("Starting backup metadata cleanup");
            backupService.delete(backupMetadataId);
            log.info("Cleanup ends successfully. Backup metadata was removed");
        } catch (Exception e) {
            progressBar.updateLabel(getTranslation("backup-dialog.backup-cleanup-error"));
            log.error("Error during backup cleanup", e);
            log.warn("Backup metadata cannot be removed.");
        }
    }
}

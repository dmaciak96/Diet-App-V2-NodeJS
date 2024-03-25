package pl.daveproject.dietapp.backup.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.apache.commons.lang3.StringUtils;
import pl.daveproject.dietapp.backup.BackupRunner;
import pl.daveproject.dietapp.backup.model.BackupMetadataDto;
import pl.daveproject.dietapp.backup.service.BackupService;
import pl.daveproject.dietapp.security.service.UserService;
import pl.daveproject.dietapp.ui.component.DeleteConfirmDialog;
import pl.daveproject.dietapp.ui.component.DownloadButton;
import pl.daveproject.dietapp.ui.component.filecomponent.FileDownloader;
import pl.daveproject.dietapp.ui.component.grid.CrudGrid;
import pl.daveproject.dietapp.ui.component.grid.GridDataFilter;
import pl.daveproject.dietapp.ui.layout.AfterLoginAppLayout;

@PermitAll
@Route(value = "/backup", layout = AfterLoginAppLayout.class)
public class BackupView extends VerticalLayout implements HasDynamicTitle {
    private static final String CREATE_BACKUP_TRANSLATION_KEY = "backup-page.new";
    private static final String DELETE_BACKUP_TRANSLATION_KEY = "backup-page.delete";
    private static final String RUN_RESTORE_TRANSLATION_KEY = "backup-page.run-restore";

    private final CrudGrid<BackupMetadataDto, GridDataFilter> backupMetadataGrid;
    private final BackupService backupService;
    private final BackupRunner backupRunner;
    private final UserService userService;

    public BackupView(BackupService backupService,
                      BackupDataProvider backupDataProvider,
                      BackupRunner backupRunner,
                      UserService userService) {
        this.backupService = backupService;
        this.backupMetadataGrid = new CrudGrid<>(backupDataProvider, null,
                CREATE_BACKUP_TRANSLATION_KEY, VaadinIcon.PLAY, DELETE_BACKUP_TRANSLATION_KEY,
                false);
        this.backupRunner = backupRunner;
        this.userService = userService;

        createGridColumns();
        setOnNewClickListener();
        setOnDeleteClickListener();
        backupMetadataGrid.addComponentToToolbar(createRunRestoreButton());
        add(backupMetadataGrid);
    }

    private void createGridColumns() {
        backupMetadataGrid.getGrid()
                .addColumn(BackupMetadataDto::getCreatedDateAsString)
                .setHeader(getTranslation("backup-page.grid.creation-date"))
                .setSortable(false)
                .setResizable(false);

        backupMetadataGrid.getGrid().addColumn(new ComponentRenderer<>(backupMetadataDto -> {
                    var downloadButton = new DownloadButton();
                    downloadButton.addClickListener(e -> {
                        var zipFileName = "backup_%s.zip".formatted(backupMetadataDto.getCreatedDateAsStringWithoutSpace());
                        var zipFileData = backupService.generateZipFromBackupData(backupMetadataDto.id());
                        add(new FileDownloader(zipFileName, zipFileData));
                    });
                    return downloadButton;
                }))
                .setHeader(StringUtils.EMPTY)
                .setSortable(false)
                .setResizable(false);
    }

    private void setOnNewClickListener() {
        backupMetadataGrid.addOnClickListener(event -> createAndOpenBackupDialog());
    }

    private void createAndOpenBackupDialog() {
        var backupDialog = new RunBackupDialog(backupRunner, backupService, userService);
        add(backupDialog);
        backupDialog.open();
        backupDialog.addOpenedChangeListener(e -> {
            if (!e.isOpened()) {
                backupMetadataGrid.refresh();
            }
        });
    }

    private void setOnDeleteClickListener() {
        backupMetadataGrid.deleteOnClickListener(event -> {
            var selected = backupMetadataGrid.getGrid()
                    .getSelectedItems()
                    .stream()
                    .findFirst();
            selected.ifPresent(this::createAndOpenDeleteDialog);
        });
    }

    private void createAndOpenDeleteDialog(BackupMetadataDto backupMetadataDto) {
        var confirmDialogSuffix = "%s \"%s\"".formatted(
                getTranslation("delete-dialog.header-backup-suffix"),
                backupMetadataDto.getCreatedDateAsString());
        var confirmDialog = new DeleteConfirmDialog(confirmDialogSuffix);
        confirmDialog.open();
        confirmDialog.addConfirmListener(event -> {
            backupService.delete(backupMetadataDto.id());
            backupMetadataGrid.refresh();
            confirmDialog.close();
        });
    }

    private Button createRunRestoreButton() {
        var restoreButton = new Button(getTranslation(RUN_RESTORE_TRANSLATION_KEY),
                new Icon(VaadinIcon.PLAY));
        restoreButton.addClickListener(event -> createAndOpenRestoreDialog());
        return restoreButton;
    }

    private void createAndOpenRestoreDialog() {
        var restoreDialog = new RunRestoreDialog();
        add(restoreDialog);
        restoreDialog.open();
        restoreDialog.addOpenedChangeListener(e -> {
            if (!e.isOpened()) {
                backupMetadataGrid.refresh();
            }
        });
    }

    @Override
    public String getPageTitle() {
        return getTranslation("backup-page.title");
    }

}

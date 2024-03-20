package pl.daveproject.dietapp.backup;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.daveproject.dietapp.backup.model.BackupMetadataDto;
import pl.daveproject.dietapp.backup.service.BackupConverter;
import pl.daveproject.dietapp.backup.service.BackupService;
import pl.daveproject.dietapp.security.service.UserService;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class BackupRunnerJsonImpl implements BackupRunner {

    private final BackupService backupService;
    private final BackupConverter backupConverter;
    private final UserService userService;

    @Override
    public void runBackup() {
        log.info("Starting backup for user {}", userService.getCurrentUser().getEmail());
        var backupMetadata = backupService.save(BackupMetadataDto.builder().build());
        log.info("Backup metadata created {}", backupMetadata.id());
        executeBackup(backupMetadata.id());
    }

    private void executeBackup(UUID backupMetadataId) {
        try {
            backupService.update(backupMetadataId, BackupMetadataDto.builder()
                    .products(backupConverter.convertProductsToByteArray())
                    .build());
            log.info("Products backup data was created");
            log.debug("Metadata updated: {}", backupMetadataId);

            backupService.update(backupMetadataId, BackupMetadataDto.builder()
                    .recipes(backupConverter.convertRecipesToByteArray())
                    .build());
            log.info("Recipes backup data was created");
            log.debug("Metadata updated: {}", backupMetadataId);

            backupService.update(backupMetadataId, BackupMetadataDto.builder()
                    .shoppingLists(backupConverter.convertShoppingListsToByteArray())
                    .build());
            log.info("Shopping lists backup data was created");
            log.debug("Metadata updated: {}", backupMetadataId);

            backupService.update(backupMetadataId, BackupMetadataDto.builder()
                    .bmi(backupConverter.convertBmiToByteArray())
                    .build());
            log.info("Bmi backup data was created");
            log.debug("Metadata updated: {}", backupMetadataId);

            backupService.update(backupMetadataId, BackupMetadataDto.builder()
                    .caloricNeeds(backupConverter.convertCaloricNeedsToByteArray())
                    .build());
            log.info("Total caloric needs backup data was created");
            log.debug("Metadata updated: {}", backupMetadataId);

            log.info("Backup for user {} was finished", userService.getCurrentUser().getEmail());
        } catch (Exception e) {
            log.error("Error during backup execution", e);
            backupService.delete(backupMetadataId);
        }
    }
}

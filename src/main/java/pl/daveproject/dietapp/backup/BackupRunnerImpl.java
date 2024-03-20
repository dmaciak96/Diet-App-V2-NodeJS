package pl.daveproject.dietapp.backup;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import pl.daveproject.dietapp.backup.model.BackupMetadataDto;
import pl.daveproject.dietapp.backup.model.Result;
import pl.daveproject.dietapp.backup.model.StepResult;
import pl.daveproject.dietapp.backup.service.BackupConverter;
import pl.daveproject.dietapp.backup.service.BackupService;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class BackupRunnerImpl implements BackupRunner {

    private final BackupService backupService;
    private final BackupConverter backupConverter;

    @Override
    public StepResult backupProducts(UUID backupMetadataId) {
        try {
            backupService.update(backupMetadataId, BackupMetadataDto.builder()
                    .products(backupConverter.convertProductsToByteArray())
                    .build());
            log.info("Products backup data was created");
            log.debug("Metadata updated: {}", backupMetadataId);
            return new StepResult(Result.SUCCESS, StringUtils.EMPTY, null);
        } catch (Exception e) {
            log.error("Error during products backup execution", e);
            return new StepResult(Result.FAILED, e.getMessage(), e);
        }
    }

    @Override
    public StepResult backupRecipes(UUID backupMetadataId) {
        try {
            backupService.update(backupMetadataId, BackupMetadataDto.builder()
                    .recipes(backupConverter.convertRecipesToByteArray())
                    .build());
            log.info("Recipes backup data was created");
            log.debug("Metadata updated: {}", backupMetadataId);
            return new StepResult(Result.SUCCESS, StringUtils.EMPTY, null);
        } catch (Exception e) {
            log.error("Error during recipes backup execution", e);
            return new StepResult(Result.FAILED, e.getMessage(), e);
        }
    }

    @Override
    public StepResult backupShoppingLists(UUID backupMetadataId) {
        try {
            backupService.update(backupMetadataId, BackupMetadataDto.builder()
                    .shoppingLists(backupConverter.convertShoppingListsToByteArray())
                    .build());
            log.info("Shopping lists backup data was created");
            log.debug("Metadata updated: {}", backupMetadataId);
            return new StepResult(Result.SUCCESS, StringUtils.EMPTY, null);
        } catch (Exception e) {
            log.error("Error during shopping lists backup execution", e);
            return new StepResult(Result.FAILED, e.getMessage(), e);
        }
    }

    @Override
    public StepResult backupBmi(UUID backupMetadataId) {
        try {
            backupService.update(backupMetadataId, BackupMetadataDto.builder()
                    .bmi(backupConverter.convertBmiToByteArray())
                    .build());
            log.info("Bmi backup data was created");
            log.debug("Metadata updated: {}", backupMetadataId);
            return new StepResult(Result.SUCCESS, StringUtils.EMPTY, null);
        } catch (Exception e) {
            log.error("Error during bmi backup execution", e);
            return new StepResult(Result.FAILED, e.getMessage(), e);
        }
    }

    @Override
    public StepResult backupTotalCaloricNeeds(UUID backupMetadataId) {
        try {
            backupService.update(backupMetadataId, BackupMetadataDto.builder()
                    .caloricNeeds(backupConverter.convertCaloricNeedsToByteArray())
                    .build());
            log.info("Total caloric needs backup data was created");
            log.debug("Metadata updated: {}", backupMetadataId);
            return new StepResult(Result.SUCCESS, StringUtils.EMPTY, null);
        } catch (Exception e) {
            log.error("Error during total caloric needs backup execution", e);
            return new StepResult(Result.FAILED, e.getMessage(), e);
        }
    }
}

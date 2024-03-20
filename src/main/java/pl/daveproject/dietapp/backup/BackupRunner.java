package pl.daveproject.dietapp.backup;

import pl.daveproject.dietapp.backup.model.StepResult;

import java.util.UUID;

public interface BackupRunner {
    StepResult backupProducts(UUID backupMetadataId);

    StepResult backupRecipes(UUID backupMetadataId);

    StepResult backupShoppingLists(UUID backupMetadataId);

    StepResult backupBmi(UUID backupMetadataId);

    StepResult backupTotalCaloricNeeds(UUID backupMetadataId);
}

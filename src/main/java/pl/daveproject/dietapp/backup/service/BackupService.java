package pl.daveproject.dietapp.backup.service;

public interface BackupService {

    //TODO: IMPLEMENT MAPPER METHODS toEntityFromBackupDto, toBackupDtoFromEntity
    //TODO: Set current user for all objects during entity saving
    //TODO: We can use DTO Objects
    byte[] convertProductsToByteArray();

    byte[] convertRecipesToByteArray();

    byte[] convertShoppingListsToByteArray();

    byte[] convertBmiToByteArray();

    byte[] convertCaloricNeedsToByteArray();
}

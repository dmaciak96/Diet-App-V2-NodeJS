package pl.daveproject.dietapp.backup.service;

public interface BackupConverter {
    byte[] convertProductsToByteArray();

    byte[] convertRecipesToByteArray();

    byte[] convertShoppingListsToByteArray();

    byte[] convertBmiToByteArray();

    byte[] convertCaloricNeedsToByteArray();
}

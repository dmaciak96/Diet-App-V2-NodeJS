package pl.daveproject.dietapp.backup;

import pl.daveproject.dietapp.backup.model.StepResult;

import java.util.UUID;

public interface RestoreRunner {

    StepResult restoreProducts(byte[] productsData);

    StepResult restoreRecipes(byte[] productsData);

    StepResult restoreShoppingLists(byte[] productsData);

    StepResult restoreBmi(byte[] productsData);

    StepResult restoreTotalCaloricNeeds(byte[] productsData);
}

package pl.daveproject.dietapp.backup;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.daveproject.dietapp.backup.model.StepResult;

@Slf4j
@Component
public class RestoreRunnerImpl implements RestoreRunner {

    @Override
    public StepResult restoreProducts(byte[] productsData) {
        return null;
    }

    @Override
    public StepResult restoreRecipes(byte[] productsData) {
        return null;
    }

    @Override
    public StepResult restoreShoppingLists(byte[] productsData) {
        return null;
    }

    @Override
    public StepResult restoreBmi(byte[] productsData) {
        return null;
    }

    @Override
    public StepResult restoreTotalCaloricNeeds(byte[] productsData) {
        return null;
    }
}

package pl.daveproject.webdiet.recipe.ui;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.theme.lumo.LumoUtility;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import pl.daveproject.webdiet.exception.ProductNotFoundException;
import pl.daveproject.webdiet.product.service.ProductService;
import pl.daveproject.webdiet.recipe.model.RecipeRequest;
import pl.daveproject.webdiet.recipe.model.RecipeType;
import pl.daveproject.webdiet.recipe.model.productentry.RecipeProductEntryRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Uses(TextArea.class)
public class RecipeForm extends FormLayout {

    private final Binder<RecipeRequest> binder;
    private final TextField name;
    private final ComboBox<RecipeType> type;
    private final TextArea description;
    private final VerticalLayout productsContainer;
    private final Button addProductButton;
    private final Button removeProductButton;
    private final ProductService productService;

    public RecipeForm(RecipeRequest recipeRequest, ProductService productService) {
        this.productService = productService;
        this.name = new TextField(getTranslation("recipe-form.name-label"));
        this.type = new ComboBox<>(getTranslation("recipe-form.type-label"));
        this.description = new TextArea(getTranslation("recipe-form-description"));
        this.type.setItems(RecipeType.values());
        this.type.setItemLabelGenerator(e -> getTranslation(e.getTranslationKey()));
        this.productsContainer = new VerticalLayout();
        this.productsContainer.setWidthFull();
        this.productsContainer.setPadding(false);
        this.productsContainer.setSpacing(false);
        this.addProductButton = new Button(getTranslation("recipe-form-add-product-button"),
                e -> addProductLayout());
        this.removeProductButton = new Button(getTranslation("recipe-form-remove-product-button"),
                e -> removeLastProductLayout());
        this.removeProductButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        this.binder = new BeanValidationBinder<>(RecipeRequest.class);
        this.binder.setBean(recipeRequest);
        this.binder.bindInstanceFields(this);

        add(name, type);
        add(description, 2);
        add(productsContainer, 2);
        createSubmitButtons();

        for (var productEntry : recipeRequest.getProducts()) {
            addProductLayout(productEntry.getProductId(), productEntry.getAmountInGrams());
        }
    }

    private void addProductLayout() {
        addProductLayout(null, null);
    }

    private void addProductLayout(UUID productId, Double amountInGramsVal) {
        var productName = new TextField(getTranslation("products-page.grid-label-name"));
        productName.setWidth("50%");
        var amountInGrams = new NumberField(getTranslation("recipe-form-amount-in-grams"));
        amountInGrams.setWidth("50%");
        amountInGrams.setMin(0.0);
        if (productId != null) {
            var productDto = productService.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException(productId));
            productName.setValue(productDto.getName());
        }
        if (amountInGramsVal != null) {
            amountInGrams.setValue(amountInGramsVal);
        }
        var productLayout = new HorizontalLayout(productName, amountInGrams);
        productLayout.setWidthFull();
        productsContainer.add(productLayout);
    }

    private void removeLastProductLayout() {
        var component = productsContainer.getComponentAt(productsContainer.getComponentCount() - 1);
        productsContainer.remove(component);
    }

    private void createSubmitButtons() {
        var save = new Button(getTranslation("form.save"));
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickShortcut(Key.ENTER);
        save.addClickListener(event -> validateAndSave());

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        var buttonLayout = new HorizontalLayout(addProductButton, removeProductButton, save);
        buttonLayout.addClassNames(LumoUtility.Margin.Top.MEDIUM);
        add(buttonLayout);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            binder.getBean().setProducts(buildRecipeProductEntries());
            fireEvent(new RecipeForm.SaveEvent(this, binder.getBean()));
        }
    }

    private List<RecipeProductEntryRequest> buildRecipeProductEntries() {
        var products = new ArrayList<RecipeProductEntryRequest>();
        for (var i = 0; i < productsContainer.getComponentCount(); i++) {
            var productLayout = (HorizontalLayout) productsContainer.getComponentAt(i);
            var product = createProductFromLayout(productLayout);
            product.ifPresent(products::add);
        }
        return products;
    }

    private Optional<RecipeProductEntryRequest> createProductFromLayout(HorizontalLayout productLayout) {
        var productName = ((TextField) productLayout.getComponentAt(0)).getValue();
        var productAmountInGrams = ((NumberField) productLayout.getComponentAt(1)).getValue();
        if (StringUtils.isBlank(productName) || productAmountInGrams == null) {
            return Optional.empty();
        }
        return Optional.of(RecipeProductEntryRequest.builder()
                .amountInGrams(productAmountInGrams)
                .productName(productName)
                .build());
    }

    @Getter
    public static abstract class RecipeFormEvent extends ComponentEvent<RecipeForm> {

        private final RecipeRequest recipeRequest;

        protected RecipeFormEvent(RecipeForm source, RecipeRequest recipeRequest) {
            super(source, false);
            this.recipeRequest = recipeRequest;
        }
    }

    public static class SaveEvent extends RecipeForm.RecipeFormEvent {

        SaveEvent(RecipeForm source, RecipeRequest recipeRequest) {
            super(source, recipeRequest);
        }
    }

    public Registration addSaveListener(ComponentEventListener<RecipeForm.SaveEvent> listener) {
        return addListener(RecipeForm.SaveEvent.class, listener);
    }
}

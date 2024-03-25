package pl.daveproject.dietapp.security.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ErrorLevel;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import lombok.extern.slf4j.Slf4j;
import pl.daveproject.dietapp.security.model.ApplicationUser;
import pl.daveproject.dietapp.security.service.UserService;
import pl.daveproject.dietapp.ui.component.MessageDialog;
import pl.daveproject.dietapp.ui.component.filecomponent.PhotoFileUploader;
import pl.daveproject.dietapp.ui.layout.BeforeLoginAppLayout;

import java.io.IOException;

@Slf4j
@AnonymousAllowed
@Route(value = "/sign-up", layout = BeforeLoginAppLayout.class)
public class RegistrationView extends VerticalLayout implements HasDynamicTitle {
    private final UserService userService;
    private final Binder<ApplicationUser> binder = new Binder<>(ApplicationUser.class);

    public RegistrationView(UserService userService) {
        this.userService = userService;
        this.binder.setBean(ApplicationUser.builder().build());
        this.setSizeFull();
        this.setAlignItems(Alignment.CENTER);
        this.setJustifyContentMode(JustifyContentMode.CENTER);

        createPageTitle();
        createRegistrationForm();
        createSignUpButton();
    }

    private void createPageTitle() {
        var h1 = new H1(getTranslation("register-form.submit"));
        add(h1);
    }

    private void createRegistrationForm() {
        var registrationForm = new VerticalLayout();
        registrationForm.setWidth("30%");

        var passwordField = new PasswordField(getTranslation("register-form.password"));
        passwordField.setRequired(true);
        passwordField.setWidth("100%");

        var firstName = new TextField(getTranslation("register-form.first-name"));
        firstName.setRequired(true);
        firstName.setWidth("100%");

        var lastName = new TextField(getTranslation("register-form.last-name"));
        lastName.setRequired(true);
        lastName.setWidth("100%");

        var email = new EmailField(getTranslation("register-form.email"));
        email.setRequired(true);
        email.setWidth("100%");

        binder.forField(email)
                .withValidator(e -> !userService.existsByEmail(e),
                        getTranslation("register-form.login-already-exists"), ErrorLevel.ERROR)
                .asRequired(getTranslation("register-form.required"))
                .bind(ApplicationUser::getEmail, ApplicationUser::setEmail);

        binder.forField(passwordField)
                .asRequired(getTranslation("register-form.required"))
                .bind(ApplicationUser::getPassword, ApplicationUser::setPassword);

        binder.forField(firstName)
                .asRequired(getTranslation("register-form.required"))
                .bind(ApplicationUser::getFirstName, ApplicationUser::setFirstName);

        binder.forField(lastName)
                .asRequired(getTranslation("register-form.required"))
                .bind(ApplicationUser::getLastName, ApplicationUser::setLastName);

        registrationForm.add(email, passwordField, firstName, lastName, createUploadButton());
        add(registrationForm);
    }

    private void createSignUpButton() {
        var signUpButton = new Button(getTranslation("register-form.submit"));
        signUpButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        signUpButton.addClickListener(event -> {
            binder.validate();
            if (binder.isValid()) {
                userService.registerNewUser(binder.getBean());
                UI.getCurrent().navigate(LoginView.class);
            }
        });
        add(signUpButton);
    }

    private Upload createUploadButton() {
        var buffer = new MemoryBuffer();
        var photoFileUploader = new PhotoFileUploader(buffer);
        photoFileUploader.setWidth("100%");
        photoFileUploader.setMaxFiles(1);
        photoFileUploader.addSucceededListener(
                event -> {
                    var inputStream = buffer.getInputStream();
                    try {
                        binder.getBean().setAvatar(inputStream.readAllBytes());
                    } catch (IOException e) {
                        new MessageDialog(getTranslation("upload.error-message"));
                    }
                });
        return photoFileUploader;
    }

    @Override
    public String getPageTitle() {
        return getTranslation("registration.title");
    }
}

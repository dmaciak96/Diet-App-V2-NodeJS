package pl.daveproject.dietapp.security.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import lombok.extern.slf4j.Slf4j;
import pl.daveproject.dietapp.configuration.LocalizationProvider;
import pl.daveproject.dietapp.ui.component.CustomLoginForm;
import pl.daveproject.dietapp.ui.layout.BeforeLoginAppLayout;

@Slf4j
@AnonymousAllowed
@Route(value = "/login", layout = BeforeLoginAppLayout.class)
public class LoginView extends VerticalLayout implements HasDynamicTitle, BeforeEnterObserver {

    private final CustomLoginForm loginForm = new CustomLoginForm();

    public LoginView() {
        var loginPageTranslation = "{\"form\": {\"title\": \"%s\",\"username\": \"%s\",\"password\": \"%s\",\"submit\": \"%s\",\"forgotPassword\": \"%s\"},\"errorMessage\": {\"title\": \"%s\",\"message\": \"%s\"}}\n"
                .formatted(getTranslation("login.title"),
                        getTranslation("login.user"),
                        getTranslation("login.password"),
                        getTranslation("login.submit"),
                        getTranslation("login.forgot-password"),
                        getTranslation("login.error-title"),
                        getTranslation("login.error-message"));

        loginForm.setI18n(LocalizationProvider.getI18n(loginPageTranslation, LoginI18n.class));
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        loginForm.setAction("login");
        loginForm.setForgotPasswordButtonVisible(false);
        loginForm.addCustomComponent(createRegisterButton());
        add(loginForm);
    }

    private Button createRegisterButton() {
        var registerButton = new Button(getTranslation("login-page-register-btn"));
        registerButton.setWidth("100%");
        registerButton.getStyle().set("margin-top", "var(--lumo-space-l)");
        registerButton.addClickListener(e -> UI.getCurrent().navigate(RegistrationView.class));
        return registerButton;
    }

    @Override
    public String getPageTitle() {
        return getTranslation("login-page.login-title");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            loginForm.setError(true);
        }
    }
}

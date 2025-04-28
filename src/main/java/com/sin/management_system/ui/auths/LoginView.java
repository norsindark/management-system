package com.sin.management_system.ui.auths;

import com.sin.management_system.applications.auths.LoginRequest;
import com.sin.management_system.applications.auths.LoginResult;
import com.sin.management_system.applications.users.UserService;
import com.sin.management_system.infrastructures.exceptions.InvalidLoginException;
import com.sin.management_system.infrastructures.utils.Constants;
import com.sin.management_system.infrastructures.utils.NotificationService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Route(value = "login")
@AnonymousAllowed
@RequiredArgsConstructor
public class LoginView extends VerticalLayout {
    private final LoginViewService loginViewService;
    private final Binder<LoginRequest> binder = new Binder<>(LoginRequest.class);

    private TextField identifierField;
    private PasswordField passwordField;
    private Button loginButton;

    @PostConstruct
    public void init() {
        setSizeFull();
        initBackground();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setUpForm();
    }

    public void setUpForm() {
        initField();
        initButton();
        initLayout();
        setUpValidation();
    }

    private void initBackground() {
        getStyle()
                .set("background-image", "url('" + Constants.BACKGROUND_IMAGE_PATH + "')")
                .set("background-size", "cover")
                .set("background-position", "center")
                .set("height", "100vh");
    }

    private void initField() {
        identifierField = new TextField("Username or email");
        passwordField = new PasswordField("Password");
    }

    private void initButton() {
        loginButton = new Button("Login", event -> handleLogin());
        loginButton.setEnabled(false);
        loginButton.getStyle().set("margin", "18px");
    }

    private void initLayout() {
        H2 title = new H2("Login");

        FormLayout formLayout = new FormLayout();
        formLayout.setWidth("400px");
        formLayout.getStyle()
                .set("padding", "20px")
                .set("background", "rgba(255, 255, 255, 0.2)")
                .set("border", "1px solid rgba(255, 255, 255, 0.3)")
                .set("border-radius", "10px")
                .set("backdrop-filter", "blur(10px)");

        formLayout.add(
                identifierField,
                passwordField,
                loginButton
        );

        VerticalLayout formWrapper = new VerticalLayout(formLayout);
        formWrapper.setWidth("auto");
        formWrapper.setAlignItems(Alignment.CENTER);
        formWrapper.setJustifyContentMode(JustifyContentMode.CENTER);

        add(title, formWrapper);
    }

    public void setUpValidation() {
        binder.forField(identifierField)
                .asRequired("Username or email is required!")
                .withValidationStatusHandler(statusChange -> {
                    identifierField.setErrorMessage(statusChange.getMessage().orElse("Invalid username or email!"));
                    identifierField.setInvalid(statusChange.isError());
                })
                .bind(LoginRequest::getIdentifier, LoginRequest::setIdentifier);

        binder.forField(passwordField)
                .asRequired("Password is required!")
                .withValidationStatusHandler(statusChange -> {
                    passwordField.setErrorMessage(statusChange.getMessage().orElse("Invalid password!"));
                    passwordField.setInvalid(statusChange.isError());
                })
                .bind(LoginRequest::getPassword, LoginRequest::setPassword);

        binder.addStatusChangeListener(event ->
                loginButton.setEnabled(binder.isValid()));
    }

    public void handleLogin() {
        LoginResult result = loginViewService.handleLoginUser(buildLoginForm());

        if (result.isSuccess() && binder.isValid()) {
            navigateToHome();
            loginViewService.showLoginSuccess();
        }
        handleLoginError(result);
    }

    private void handleLoginError(LoginResult result) {
        if ("identifier".equals(result.getErrorField())) {
            identifierField.setInvalid(true);
            identifierField.setErrorMessage(result.getErrorMessage());
        } else if ("password".equals(result.getErrorField())) {
            passwordField.setInvalid(true);
            passwordField.setErrorMessage(result.getErrorMessage());
        }
    }


    public LoginRequest buildLoginForm() {
        return LoginRequest.builder()
                .identifier(identifierField.getValue())
                .password(passwordField.getValue())
                .build();
    }

    public void navigateToHome() {
        getUI().ifPresent(ui -> ui.navigate("home"));
    }
}

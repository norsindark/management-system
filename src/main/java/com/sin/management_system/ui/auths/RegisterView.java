package com.sin.management_system.ui.auths;


import com.sin.management_system.applications.auths.RegisterRequest;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import com.sin.management_system.infrastructures.utils.Constants;

@Route(value = "register")
@AnonymousAllowed
@RequiredArgsConstructor
public class RegisterView extends VerticalLayout {
    private final Binder<RegisterRequest> binder = new Binder<>(RegisterRequest.class);
    private final RegisterViewService registerViewService;

    private TextField usernameField;
    private TextField fullNameField;
    private EmailField emailField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private Button registerButton;

    @PostConstruct
    private void init() {
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

    private void setUpValidation() {
        binder.forField(fullNameField)
                .asRequired("Full name is required!")
                .withValidationStatusHandler(status -> {
                    fullNameField.setErrorMessage(status.getMessage().orElse("Invalid full name"));
                    fullNameField.setInvalid(status.isError());
                })
                .bind(RegisterRequest::getFullName, RegisterRequest::setFullName);

        binder.forField(usernameField)
                .asRequired("Username is required!")
                .withValidationStatusHandler(status -> {
                    usernameField.setErrorMessage(status.getMessage().orElse("Invalid username"));
                    usernameField.setInvalid(status.isError());
                })
                .bind(RegisterRequest::getUsername, RegisterRequest::setUsername);

        binder.forField(emailField)
                .asRequired("Email is required")
                .withValidator(email -> email.matches("^[A-Za-z0-9+_.-]+@(.+)$"), "Invalid email address!")
                .withValidationStatusHandler(status -> {
                    emailField.setErrorMessage(status.getMessage().orElse("Invalid email address!"));
                    emailField.setInvalid(status.isError());
                })
                .bind(RegisterRequest::getEmail, RegisterRequest::setEmail);

        binder.forField(passwordField)
                .asRequired("Password is required")
                .withValidator(pwd -> pwd.length() >= 6, "Password must be at least 6 characters")
                .withValidationStatusHandler(status -> {
                    passwordField.setErrorMessage(status.getMessage().orElse("Invalid password"));
                    passwordField.setInvalid(status.isError());
                })
                .bind(RegisterRequest::getPassword, RegisterRequest::setPassword);

        binder.forField(confirmPasswordField)
                .asRequired("Confirm Password is required")
                .withValidator(confirm -> confirm.equals(passwordField.getValue()), "Passwords do not match")
                .withValidationStatusHandler(status -> {
                    confirmPasswordField.setErrorMessage(status.getMessage().orElse("Invalid password"));
                    confirmPasswordField.setInvalid(status.isError());
                })
                .bind(RegisterRequest::getConfirmPassword, RegisterRequest::setConfirmPassword);

        binder.addStatusChangeListener(event ->
                registerButton.setEnabled(binder.isValid()));
    }

    private void initBackground() {
        getStyle()
                .set("background-image", "url('" + Constants.BACKGROUND_IMAGE_PATH + "')")
                .set("background-size", "cover")
                .set("background-position", "center")
                .set("height", "100vh");
    }

    private void initField() {
        usernameField = new TextField("Username (*)");
        emailField = new EmailField("Email (*)");
        fullNameField = new TextField("Full Name (*)");
        passwordField = new PasswordField("Password (*)");
        confirmPasswordField = new PasswordField("Confirm Password (*)");
    }

    private void initButton() {
        registerButton = new Button("Register",
                event -> handleRegister());
        registerButton.setEnabled(false);
        registerButton.getStyle().set("margin", "18px");
    }

    private void handleRegister() {
        registerViewService.handleRegister(buildRegisterRequest());
        navigateToLogin();
        registerViewService.showNotificationCreateUser();
    }

    private void initLayout() {
        H2 title = new H2("Register");

        FormLayout formLayout = new FormLayout();
        formLayout.setWidth("400px");
        formLayout.getStyle()
                .set("padding", "20px")
                .set("background", "rgba(255, 255, 255, 0.2)")
                .set("border", "1px solid rgba(255, 255, 255, 0.3)")
                .set("border-radius", "10px")
                .set("backdrop-filter", "blur(10px)");

        formLayout.add(
                fullNameField,
                usernameField,
                emailField,
                passwordField,
                confirmPasswordField,
                registerButton);


        VerticalLayout formWrapper = new VerticalLayout(formLayout);
        formWrapper.setWidth("auto");
        formWrapper.setAlignItems(Alignment.CENTER);
        formWrapper.setJustifyContentMode(JustifyContentMode.CENTER);

        add(title, formWrapper);
    }

    private RegisterRequest buildRegisterRequest() {
        return RegisterRequest.builder()
                .username(usernameField.getValue())
                .email(emailField.getValue())
                .fullName(fullNameField.getValue())
                .password(passwordField.getValue())
                .confirmPassword(confirmPasswordField.getValue())
                .build();
    }

    private void navigateToLogin() {
        getUI().ifPresent(ui -> ui.navigate("login"));
    }
}

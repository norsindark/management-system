package com.sin.management_system.ui.auths;

import com.vaadin.flow.component.html.H1;

import com.sin.management_system.applications.auths.RegisterRequest;
import com.sin.management_system.applications.roles.RoleService;
import com.sin.management_system.applications.users.UserService;
import com.sin.management_system.domains.roles.Role;
import com.sin.management_system.domains.users.User;
import com.sin.management_system.infrastructures.utils.NotificationService;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import com.sin.management_system.infrastructures.utils.Constants;

@Route(value = "register")
@AnonymousAllowed
@RequiredArgsConstructor
public class RegisterView extends VerticalLayout {
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder encoder;
    private final NotificationService notificationUtil;
    private final Binder<RegisterRequest> binder = new Binder<>(RegisterRequest.class);


    private TextField usernameField;
    private TextField fullNameField;
    private EmailField emailField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private Button registerButton;

    @PostConstruct
    private void init() {
        setSizeFull();
        getStyle()
                .set("background-image", "url('" + Constants.BACKGROUND_IMAGE_PATH + "')")
                .set("background-size", "cover")
                .set("background-position", "center")
                .set("height", "100vh");
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setUpForm();

    }

    public void setUpForm() {
        usernameField = new TextField("Username (*)");
        emailField = new EmailField("Email (*)");
        fullNameField = new TextField("Full Name (*)");
        passwordField = new PasswordField("Password (*)");
        confirmPasswordField = new PasswordField("Confirm Password (*)");

        registerButton = new Button("Register", event -> registerUser());
        registerButton.setEnabled(false);
        registerButton.getStyle().set("margin", "18px");
        H1 welcome = new H1("WELCOME");

        FormLayout formLayout = new FormLayout();
        formLayout.setWidth("400px");
        formLayout.getStyle().set("padding", "20px");
        formLayout.getStyle().set("border", "1px solid #ccc");
        formLayout.getStyle().set("border-radius", "10px");
        formLayout.getStyle().set("background", "#f9f9f9");

        formLayout.add(
                fullNameField,
                usernameField,
                emailField,
                passwordField,
                confirmPasswordField,
                registerButton);
        add(welcome);

        VerticalLayout formWrapper = new VerticalLayout(formLayout);
        formWrapper.setWidth("auto");
        formWrapper.setAlignItems(Alignment.CENTER);
        formWrapper.setJustifyContentMode(JustifyContentMode.CENTER);
        add(formWrapper);
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


    private void registerUser() {
        RegisterRequest request = buildRegisterRequest();

        if (userService.existsByUsernameOrEmail(request.getUsername(), request.getEmail())) {
            notificationUtil.showNotification("Username or Email already exists!");
            return;
        }

        if (!binder.writeBeanIfValid(request)) {
            notificationUtil.showNotification("Please try again!");
        }
        createUser(request);
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

    private void createUser(RegisterRequest request) {
        Role roleDefault = roleService.getRoleDefault().orElseThrow();

        User newUser = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .fullName(request.getFullName())
                .role(roleDefault)
                .provider("local")
                .providerId(null)
                .password(encoder.encode(request.getPassword()))
                .build();
        User saveUser = userService.saveNewUser(newUser);
        if (saveUser == null) {
            notificationUtil.showNotification("Something went wrong!");
        }
        notificationUtil.showNotification("User create successful!");
        clearForm();
    }

    private void clearForm() {
        binder.readBean(new RegisterRequest());
    }

    private void navigateToLogin() {
        getUI().ifPresent(ui -> ui.navigate("login"));
    }
}

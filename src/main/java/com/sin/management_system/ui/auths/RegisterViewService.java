package com.sin.management_system.ui.auths;

import com.sin.management_system.applications.auths.RegisterRequest;
import com.sin.management_system.applications.roles.RoleService;
import com.sin.management_system.applications.users.UserService;
import com.sin.management_system.domains.roles.Role;
import com.sin.management_system.domains.users.User;
import com.sin.management_system.infrastructures.utils.NotificationService;
import com.vaadin.flow.data.binder.Binder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterViewService {
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder encoder;
    private final NotificationService notificationUtil;
    private final Binder<RegisterRequest> binder = new Binder<>(RegisterRequest.class);

    public void handleRegister(RegisterRequest request) {

        if (isDuplicateUser(request)) {
            showDuplicateUserNotification();
            return;
        }

        if (!isValidRequest(request)) {
            showInvalidRequestNotification();
            return;
        }
        saveUser(request);
    }

    public void saveUser(RegisterRequest request) {
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
        userService.saveNewUser(newUser);
        showNotificationCreateUser();
    }

    public boolean isDuplicateUser(RegisterRequest request) {
        return userService.existsByUsernameOrEmail(request.getUsername(), request.getEmail());
    }

    public boolean isValidRequest(RegisterRequest request) {
        return binder.writeBeanIfValid(request);
    }

    public void showDuplicateUserNotification() {
        notificationUtil.showNotification("Username or Email already exists!");
    }

    public void showInvalidRequestNotification() {
        notificationUtil.showNotification("Please try again!");
    }

    public void showNotificationCreateUser(){
        notificationUtil.showNotification("User create successful!");
    }
}

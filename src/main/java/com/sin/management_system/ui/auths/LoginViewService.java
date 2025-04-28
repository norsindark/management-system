package com.sin.management_system.ui.auths;

import com.sin.management_system.applications.auths.LoginRequest;
import com.sin.management_system.applications.auths.LoginResult;
import com.sin.management_system.applications.users.UserService;
import com.sin.management_system.domains.users.User;
import com.sin.management_system.infrastructures.exceptions.InvalidLoginException;
import com.sin.management_system.infrastructures.utils.NotificationService;
import com.vaadin.flow.data.binder.Binder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginViewService {
    private final UserService userService;
    private final NotificationService notificationService;
    private final PasswordEncoder encoder;
    private final Binder<LoginRequest> binder = new Binder<>(LoginRequest.class);

    public LoginResult handleLoginUser(LoginRequest request) {
        Optional<User> userOpt = userService.findByUsernameOrEmail(request.getIdentifier(), request.getIdentifier());

        if (userOpt.isEmpty()) {
            return LoginResult.failure("identifier", "Username or email not found!");
        }

        User user = userOpt.get();
        if (!isPasswordValid(request.getPassword(), user.getPassword())) {
            return LoginResult.failure("password", "Invalid password!");
        }

        setSecurityContext(user);
        return LoginResult.success();
    }

    private void setSecurityContext(User user) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private boolean isPasswordValid(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }

    public void showLoginSuccess() {
        notificationService.showNotification("Login successful!");
    }
}

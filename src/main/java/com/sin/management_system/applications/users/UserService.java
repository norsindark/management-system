package com.sin.management_system.applications.users;

import com.sin.management_system.domains.users.User;

import java.util.Optional;

public interface UserService {
    void saveNewUser(User user);

    boolean existsByUsernameOrEmail(String username, String email);

    Optional<User> findByUsernameOrEmail(String username, String email);
}

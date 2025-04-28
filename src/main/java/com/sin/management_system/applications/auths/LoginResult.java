package com.sin.management_system.applications.auths;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResult {
    private final boolean success;
    private final String errorField;
    private final String errorMessage;

    public static LoginResult success() {
        return new LoginResult(true, null, null);
    }

    public static LoginResult failure(String field, String message) {
        return new LoginResult(false, field, message);
    }
}

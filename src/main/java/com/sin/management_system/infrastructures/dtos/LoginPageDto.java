package com.sin.management_system.infrastructures.dtos;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "login.oauth2")
@Data
public class LoginPageDto {
    private String loginPage;
    private String defaultPage;
    private String failurePage;
}

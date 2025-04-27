package com.sin.management_system.infrastructures.dtos;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtPropertiesDto {
    private String secretKey;
    private long accessTokenExpirationMS;
    private long refreshTokenExpirationMS;
}

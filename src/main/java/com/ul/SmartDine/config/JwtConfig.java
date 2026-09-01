package com.ul.SmartDine.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JwtConfig.JwtProperties.class)
public class JwtConfig {
    @Getter
    @Setter
    @ConfigurationProperties(prefix = "jwt")
    public static class JwtProperties {
        private String secret;
        private long expirationMs = 36000000L;
        private long refreshExpirationMs = 72000000L;
    }
}

package com.ul.SmartDine.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(OtpConfig.OtpProperties.class)
public class OtpConfig {
    @Getter
    @Setter
    @ConfigurationProperties(prefix = "otp")
    public static class OtpProperties {
        private int expiryMinutes = 5;
        private int length = 6;
        private int maxAttempts = 3;
    }
}

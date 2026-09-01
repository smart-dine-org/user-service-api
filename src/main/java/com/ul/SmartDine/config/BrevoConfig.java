package com.ul.SmartDine.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(BrevoConfig.BrevoProperties.class)
public class BrevoConfig {
    private static final String BREVO_BASE_URL = "https://api.brevo.com/v3";

    @Bean
    public RestClient BrevoRestClient(BrevoProperties properties) {
        return RestClient.builder().baseUrl(BREVO_BASE_URL).defaultHeader("api-key", properties.getApiKey()).defaultHeader("Content-Type", "application/json").defaultHeader("Accept", "application/json").build();
    }

    @Getter
    @Setter
    @ConfigurationProperties(prefix = "brevo")
    public static class BrevoProperties {
        private String apiKey;
        private Sender sender = new Sender();
        private Templates templates = new Templates();

        @Getter
        @Setter
        public static class Sender {
            private String email;
            private String name;
        }

        @Getter
        @Setter
        public static class Templates {
            private Long otp;
            private Long welcome;
            private Long resetPassword;
        }
    }
}

package com.ul.SmartDine.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(KeycloakConfig.KeycloakProperties.class)
public class KeycloakConfig {
    @Bean
    public RestClient keycloakRestClient(KeycloakProperties properties) {
        return RestClient.builder().baseUrl(properties.getServerUrl()).build();
    }

    @Getter
    @Setter
    @ConfigurationProperties(prefix = "keycloak")
    public static class KeycloakProperties {
        private String serverUrl;
        private String realm;
        private String clientId;
        private String secretId;
        private Admin admin = new Admin();

        public static class Admin {
            private String username;
            private String password;
        }
    }
}

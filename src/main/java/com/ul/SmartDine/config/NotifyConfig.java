package com.ul.SmartDine.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(NotifyConfig.NotifyProperties.class)
public class NotifyConfig {
    @Bean
    public RestClient notifyRestClient(NotifyProperties properties) {
        return RestClient.builder().baseUrl(properties.getBaseUrl()).build();
    }

    @Getter
    @Setter
    @ConfigurationProperties(prefix = "notifylk")
    public static class NotifyProperties {
        private String userId;
        private String apiKey;
        private String senderId;
        private String baseUrl;
    }
}

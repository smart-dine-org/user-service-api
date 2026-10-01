package com.ul.SmartDine.service.impl;

import com.ul.SmartDine.config.BrevoConfig;
import com.ul.SmartDine.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final RestClient brevoRestClient;
    private final BrevoConfig.BrevoProperties brevoProperties;

    @Override
    public void sendOtpEmail(String toEmail, String toName, String otp) {
        Map<String, Object> payload = buildTemplatePayload(toEmail, toName, brevoProperties.getTemplates().getOtp(), Map.of("otpCode", otp, "expiryMinutes", 5));
        sendEmail(payload);
    }

    @Override
    public void sendWelcomeEmail(String toEmail, String toName) {
        Map<String, Object> payload = buildTemplatePayload(toEmail, toName, brevoProperties.getTemplates().getWelcome(), Map.of("firstName", toName));
        sendEmail(payload);
    }

    @Override
    public void sendResetPasswordEmail(String toEmail, String toName, String otp) {
        Map<String, Object> payload = buildTemplatePayload(toEmail, toName, brevoProperties.getTemplates().getResetPassword(), Map.of("otpCode", otp, "expiryMinutes", 5));
        sendEmail(payload);
    }

    private void sendEmail(Map<String, Object> payload) {
        brevoRestClient.post().uri("/smtp/email").body(payload).retrieve().toBodilessEntity();
    }

    private Map<String, Object> buildTemplatePayload(String toEmail, String toName, Long templateId, Map<String, Object> params) {
        return Map.of("sender", Map.of("email", brevoProperties.getSender().getEmail(), "name", brevoProperties.getSender().getName()), "to", List.of(Map.of("email", toEmail, "name", toName)), "templateId", templateId, "params", params);
    }
}

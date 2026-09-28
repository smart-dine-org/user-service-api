package com.ul.SmartDine.service;

public interface EmailService {
    void sendOtpEmail(String toEmail, String toName, String otp);
    void sendWelcomeEmail(String toEmail, String toName);
    void sendResetPasswordEmail(String toEmail, String toName, String otp);
}

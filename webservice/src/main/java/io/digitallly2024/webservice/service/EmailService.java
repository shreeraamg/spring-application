package io.digitallly2024.webservice.service;

public interface EmailService {
    void sendResetPasswordEmail(String recipient, String token);
}

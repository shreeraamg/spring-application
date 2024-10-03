package io.digitallly2024.emailservice.service;

public interface EmailService {
    void sendResetPasswordEmail(String email, String token);
}

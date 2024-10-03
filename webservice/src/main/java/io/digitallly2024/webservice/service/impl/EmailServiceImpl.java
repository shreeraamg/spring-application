package io.digitallly2024.webservice.service.impl;

import io.digitallly2024.webservice.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

// @Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderEmailId;

    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendResetPasswordEmail(String recipient, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmailId);
        message.setTo(recipient);
        message.setSubject("Spring Application: Reset Password Link");
        message.setText(token);

        javaMailSender.send(message);
    }
}

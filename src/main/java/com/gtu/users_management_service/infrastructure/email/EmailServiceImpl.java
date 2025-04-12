package com.gtu.users_management_service.infrastructure.email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.gtu.users_management_service.domain.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendCredentials(String toEmail, String name, String generatedPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-reply@gtu.com");
        message.setTo(toEmail);
        message.setSubject("Welcome to GTU - Your Credentials");
        message.setText(String.format(
                "Hello %s,\n\nYour account has been created successfully.\n\nEmail: %s\nPassword: %s\n\nPlease change your password after logging in.\n\nBest regards,\nGTU Team",
                name, toEmail, generatedPassword
        ));
        mailSender.send(message);
    }
}
package com.gtu.users_management_service.domain.service;

public interface EmailService {
    void sendCredentials(String toEmail, String email, String generatedPassword);
}

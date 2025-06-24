package com.gtu.users_management_service.application.service;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtu.users_management_service.application.dto.PasswordUpdateDTO;
import com.gtu.users_management_service.domain.exception.ResourceNotFoundException;
import com.gtu.users_management_service.domain.model.Role;
import com.gtu.users_management_service.domain.model.Status;
import com.gtu.users_management_service.domain.model.User;
import com.gtu.users_management_service.domain.repository.UserRepository;
import com.gtu.users_management_service.domain.service.UserService;
import com.gtu.users_management_service.infrastructure.messaging.event.UserCreatedEvent;
import com.gtu.users_management_service.infrastructure.security.PasswordEncoderUtil;
import com.gtu.users_management_service.infrastructure.security.PasswordValidator;
import com.gtu.users_management_service.infrastructure.logs.LogPublisher;
import java.time.Instant;

@Service
public class UserServiceImpl implements UserService {

    private static final String USER_NOT_FOUND = "User does not exist";

    private final UserRepository userRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final Logger logger = Logger.getLogger(UserServiceImpl.class.getName());
    private final LogPublisher logPublisher;

    public UserServiceImpl(UserRepository userRepository, RabbitTemplate rabbitTemplate, ObjectMapper objectMapper,
            LogPublisher logPublisher) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
        this.logPublisher = logPublisher;
    }

    @Value("${rabbitmq.exchange.email}")
    private String emailExchange;

    @Value("${rabbitmq.routingkey.email}")
    private String emailRoutingKey;

    @Override
    public User createUser(User user) {
        logPublisher.sendLog(
                Instant.now().toString(),
                "users-management-service",
                "INFO",
                "Creating user",
                Map.of("name", user.getName(), "email", user.getEmail(), "role", user.getRole()));
        if (user.getName() == null || user.getName().isEmpty()) {
            throw new IllegalArgumentException("User name cannot be empty");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (!PasswordValidator.isValid(user.getPassword())) {
            throw new IllegalArgumentException(
                    "Password must contain at least 8 characters, including uppercase letters and numbers");
        }
        if (user.getRole() == null || (user.getRole() != Role.ADMIN && user.getRole() != Role.DRIVER)) {
            throw new IllegalArgumentException("Role cannot be null or invalid");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email is already in use");
        }

        String plainPassword = user.getPassword();
        String encodedPassword = PasswordEncoderUtil.encode(plainPassword);
        user.setPassword(encodedPassword);

        user.setStatus(user.getStatus() != null ? user.getStatus() : Status.ACTIVE);

        User savedUser = userRepository.save(user);

        try {
            UserCreatedEvent event = new UserCreatedEvent();
            event.setEmail(savedUser.getEmail());
            event.setUsername(savedUser.getName());
            event.setPassword(plainPassword);
            String message = objectMapper.writeValueAsString(event);
            rabbitTemplate.convertAndSend(emailExchange, emailRoutingKey, message);
        } catch (Exception e) {
            logger.severe("Failed to send user created event: " + e.getMessage());
        }

        return savedUser;
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND));
        userRepository.deleteById(id);
    }

    @Override
    public User updateStatus(Long id, Status status) {
        if (status != Status.ACTIVE && status != Status.INACTIVE) {
            throw new IllegalArgumentException("Invalid status value. Only ACTIVE or INACTIVE are allowed.");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND));

        user.setStatus(status);
        return userRepository.save(user);
    }

    @Override
    public List<User> getUsersByRole(Role role) {
        if (role != Role.ADMIN && role != Role.DRIVER) {
            throw new IllegalArgumentException("Invalid role value. Only ADMIN or DRIVER are allowed.");
        }
        List<User> users = userRepository.findByRole(role);
        for (User user : users) {
            user.setPassword(null);
        }
        return users;
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND));
    }

    @Override
    public User updatePassword(User user, PasswordUpdateDTO passwordUpdateDTO) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Passenger not found"));
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Current password cannot be null or empty");
        }
        if (!PasswordEncoderUtil.matches(user.getPassword(), existingUser.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        if (passwordUpdateDTO.getNewPassword() == null || passwordUpdateDTO.getNewPassword().isEmpty()) {
            throw new IllegalArgumentException("New password cannot be null or empty");
        }
        if (!PasswordValidator.isValid(passwordUpdateDTO.getNewPassword())) {
            throw new IllegalArgumentException(
                    "New password must contain at least 8 characters, including uppercase letters and numbers");
        }
        existingUser.setPassword(PasswordEncoderUtil.encode(passwordUpdateDTO.getNewPassword()));
        return userRepository.save(existingUser);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
    }

    @Override
    public User resetPassword(User user, String newPassword) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND));

        if (newPassword == null || newPassword.isEmpty()) {
            throw new IllegalArgumentException("New password cannot be null or empty");
        }
        if (!PasswordValidator.isValid(newPassword)) {
            throw new IllegalArgumentException(
                    "New password must contain at least 8 characters, including uppercase letters and numbers");
        }
        existingUser.setPassword(PasswordEncoderUtil.encode(newPassword));
        return userRepository.save(existingUser);
    }
}
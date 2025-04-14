package com.gtu.users_management_service.application.service;

import com.gtu.users_management_service.domain.model.Status;
import com.gtu.users_management_service.domain.model.User;
import com.gtu.users_management_service.domain.repository.UserRepository;
import com.gtu.users_management_service.domain.service.UserService;
import com.gtu.users_management_service.infrastructure.email.EmailServiceImpl;
import com.gtu.users_management_service.infrastructure.security.PasswordEncoderUtil;
import com.gtu.users_management_service.infrastructure.security.PasswordValidator;

import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmailServiceImpl emailService;

    public UserServiceImpl(UserRepository userRepository, EmailServiceImpl emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Override
    public User createUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            throw new IllegalArgumentException("User name cannot be empty");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (!PasswordValidator.isValid(user.getPassword())) {
            throw new IllegalArgumentException("Password must contain at least 8 characters, including uppercase letters and numbers");
        }
        if (user.getRole() == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email is already in use");
        }
        String rawPassword = user.getPassword();
        String encodedPassword = PasswordEncoderUtil.encode(user.getPassword());
        user.setPassword(encodedPassword);
        
        user.setStatus(user.getStatus() != null ? user.getStatus() : Status.ACTIVE);

        User savedUser = userRepository.save(user);

        emailService.sendCredentials(
                savedUser.getEmail(),
                savedUser.getName(),
                rawPassword
        );
        return savedUser;
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User does not exist"));
        userRepository.deleteById(id);
    }

    
}
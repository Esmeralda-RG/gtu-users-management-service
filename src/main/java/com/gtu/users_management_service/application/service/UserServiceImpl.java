package com.gtu.users_management_service.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gtu.users_management_service.application.dto.PasswordUpdateDTO;
import com.gtu.users_management_service.domain.model.Role;
import com.gtu.users_management_service.domain.model.Status;
import com.gtu.users_management_service.domain.model.User;
import com.gtu.users_management_service.domain.repository.UserRepository;
import com.gtu.users_management_service.domain.service.UserService;
import com.gtu.users_management_service.infrastructure.security.PasswordEncoderUtil;
import com.gtu.users_management_service.infrastructure.security.PasswordValidator;

@Service
public class UserServiceImpl implements UserService {

    private static final String USER_NOT_FOUND = "User does not exist";

    private final UserRepository userRepository;
  

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        if (user.getRole() == null && user.getRole() != Role.ADMIN && user.getRole() != Role.DRIVER) {
            throw new IllegalArgumentException("Role cannot be null or invalid");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email is already in use");
        }
        String encodedPassword = PasswordEncoderUtil.encode(user.getPassword());
        user.setPassword(encodedPassword);
        
        user.setStatus(user.getStatus() != null ? user.getStatus() : Status.ACTIVE);


        return userRepository.save(user);
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
        if(user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Current password cannot be null or empty");
        }
        if (!PasswordEncoderUtil.matches(user.getPassword(), existingUser.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        if (passwordUpdateDTO.getNewPassword() == null || passwordUpdateDTO.getNewPassword().isEmpty()) {
            throw new IllegalArgumentException("New password cannot be null or empty");
        }
        if (!PasswordValidator.isValid(passwordUpdateDTO.getNewPassword())) {
            throw new IllegalArgumentException("New password must contain at least 8 characters, including uppercase letters and numbers");
        }
        existingUser.setPassword(PasswordEncoderUtil.encode(passwordUpdateDTO.getNewPassword()));
        return userRepository.save(existingUser);
    }
}
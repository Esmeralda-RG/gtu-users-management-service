package com.gtu.users_management_service.application.service;

import org.springframework.stereotype.Service;

import com.gtu.users_management_service.application.dto.PasswordUpdateDTO;
import com.gtu.users_management_service.domain.model.Passenger;
import com.gtu.users_management_service.domain.repository.PassengerRepository;
import com.gtu.users_management_service.domain.service.PassengerService;
import com.gtu.users_management_service.infrastructure.security.PasswordEncoderUtil;
import com.gtu.users_management_service.infrastructure.security.PasswordValidator;

@Service
public class PassengerServiceImpl implements PassengerService {

    private final PassengerRepository passengerRepository;

    private static final String NOT_FOUND_MESSAGE = "Passenger not found";

    public PassengerServiceImpl(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }

    @Override
    public Passenger createPassenger(Passenger passenger) {
        if (passenger.getName() == null || passenger.getName().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (passenger.getEmail() == null || passenger.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (passengerRepository.existsByEmail(passenger.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (!PasswordValidator.isValid(passenger.getPassword())) {
            throw new IllegalArgumentException("Password must contain at least 8 characters, including uppercase letters and numbers");
        }
        
        passenger.setPassword(PasswordEncoderUtil.encode(passenger.getPassword()));
        return passengerRepository.save(passenger);
    }

    @Override
    public Passenger updatePassenger(Passenger passenger) {
        Passenger existingPassenger = passengerRepository.findById(passenger.getId())
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_MESSAGE));
        if (passenger.getName() != null && !passenger.getName().isEmpty()) {
            existingPassenger.setName(passenger.getName());
        }
        if (passenger.getEmail() != null && !passenger.getEmail().isEmpty()) {
            if (passengerRepository.existsByEmail(passenger.getEmail())) {
                throw new IllegalArgumentException("Email already exists");
            }
            existingPassenger.setEmail(passenger.getEmail());
        }
        if (passenger.getPassword() != null) {
            throw new IllegalArgumentException("Password cannot be updated");
        }
        return passengerRepository.save(existingPassenger);
    }

    @Override
    public Passenger updatePassword(Passenger passenger, PasswordUpdateDTO passwordUpdateDTO) {
        Passenger existingPassenger = passengerRepository.findById(passenger.getId())
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_MESSAGE));
        if(passenger.getPassword() == null || passenger.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Current password cannot be null or empty");
        }
        if (!PasswordEncoderUtil.matches(passenger.getPassword(), existingPassenger.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        if (passwordUpdateDTO.getNewPassword() == null || passwordUpdateDTO.getNewPassword().isEmpty()) {
            throw new IllegalArgumentException("New password cannot be null or empty");
        }
        if (!PasswordValidator.isValid(passwordUpdateDTO.getNewPassword())) {
            throw new IllegalArgumentException("New password must contain at least 8 characters, including uppercase letters and numbers");
        }
        existingPassenger.setPassword(PasswordEncoderUtil.encode(passwordUpdateDTO.getNewPassword()));
        return passengerRepository.save(existingPassenger);
    }

    @Override
    public Long countPassengers() {
        return passengerRepository.count();
    }

    @Override
    public Passenger getPassengerByEmail(String email) {
       return passengerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_MESSAGE));
    }

    @Override
    public Passenger resetPassword(Passenger passenger, String newPassword) {
        Passenger existingPassenger = passengerRepository.findById(passenger.getId())
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_MESSAGE));

        if (newPassword == null || newPassword.isEmpty()) {
            throw new IllegalArgumentException("New password cannot be null or empty");
        }
        if (!PasswordValidator.isValid(newPassword)) {
            throw new IllegalArgumentException("New password must contain at least 8 characters, including uppercase letters and numbers");
        }
        existingPassenger.setPassword(PasswordEncoderUtil.encode(newPassword));
        return passengerRepository.save(existingPassenger);
    }
}

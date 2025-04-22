package com.gtu.users_management_service.application.service;

import org.springframework.stereotype.Service;

import com.gtu.users_management_service.domain.model.Passenger;
import com.gtu.users_management_service.domain.repository.PassengerRepository;
import com.gtu.users_management_service.domain.service.PassengerService;
import com.gtu.users_management_service.infrastructure.security.PasswordEncoderUtil;
import com.gtu.users_management_service.infrastructure.security.PasswordValidator;

@Service
public class PassengerServiceImpl implements PassengerService {

    private final PassengerRepository passengerRepository;

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
}

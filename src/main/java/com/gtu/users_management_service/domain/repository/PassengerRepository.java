package com.gtu.users_management_service.domain.repository;

import java.util.Optional;

import com.gtu.users_management_service.domain.model.Passenger;

public interface PassengerRepository {
    Passenger save(Passenger passenger);
    boolean existsByEmail(String email);
    boolean existsById(Long id);
    Optional<Passenger> findById(Long id);
}

package com.gtu.users_management_service.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gtu.users_management_service.infrastructure.entities.PassengerEntity;

@Repository
public interface JpaPassengerRepository extends JpaRepository<PassengerEntity, Long> {
    boolean existsByEmail(String email);
}

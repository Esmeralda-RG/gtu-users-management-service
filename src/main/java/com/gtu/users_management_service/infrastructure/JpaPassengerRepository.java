package com.gtu.users_management_service.infrastructure;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gtu.users_management_service.infrastructure.entities.PassengerEntity;

@Repository
public interface JpaPassengerRepository extends JpaRepository<PassengerEntity, Long> {
    @Query("SELECT p FROM PassengerEntity p WHERE p.email = ?1")
    Optional<PassengerEntity> findByEmail(String email);
}

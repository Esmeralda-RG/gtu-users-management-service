package com.gtu.users_management_service.infrastructure.mappers;

import com.gtu.users_management_service.domain.model.Passenger;
import com.gtu.users_management_service.infrastructure.entities.PassengerEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PassengerEntityMapperTest {

    @Test
    void shouldMapPassengerToEntityCorrectly() {
        Passenger passenger = new Passenger(1L, "John Doe", "john@example.com", "securePassword");

        PassengerEntity entity = PassengerEntityMapper.toEntity(passenger);

        assertNotNull(entity);
        assertEquals(passenger.getId(), entity.getId());
        assertEquals(passenger.getName(), entity.getName());
        assertEquals(passenger.getEmail(), entity.getEmail());
        assertEquals(passenger.getPassword(), entity.getPassword());
    }

    @Test
    void shouldMapEntityToPassengerCorrectly() {
        PassengerEntity entity = new PassengerEntity(2L, "Jane Smith", "jane@example.com", "encryptedPass");

        Passenger passenger = PassengerEntityMapper.toDomain(entity);

        assertNotNull(passenger);
        assertEquals(entity.getId(), passenger.getId());
        assertEquals(entity.getName(), passenger.getName());
        assertEquals(entity.getEmail(), passenger.getEmail());
        assertEquals(entity.getPassword(), passenger.getPassword());
    }
}

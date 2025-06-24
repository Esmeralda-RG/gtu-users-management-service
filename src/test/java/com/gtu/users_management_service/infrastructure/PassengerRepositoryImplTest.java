package com.gtu.users_management_service.infrastructure;

import com.gtu.users_management_service.domain.model.Passenger;
import com.gtu.users_management_service.infrastructure.entities.PassengerEntity;
import com.gtu.users_management_service.infrastructure.mappers.PassengerEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PassengerRepositoryImplTest {

    private JpaPassengerRepository jpaPassengerRepository;
    private PassengerRepositoryImpl passengerRepository;

    @BeforeEach
    void setUp() {
        jpaPassengerRepository = mock(JpaPassengerRepository.class);
        passengerRepository = new PassengerRepositoryImpl(jpaPassengerRepository);
    }

    @Test
    void shouldSaveAndReturnPassenger_whenPassengerIsValid() {
        Passenger passenger = new Passenger();
        PassengerEntity entity = PassengerEntityMapper.toEntity(passenger);
        when(jpaPassengerRepository.save(any())).thenReturn(entity);

        Passenger result = passengerRepository.save(passenger);

        assertNotNull(result);
        verify(jpaPassengerRepository).save(any());
    }

    @Test
    void shouldReturnTrue_whenPassengerExistsByEmail() {
        String email = "test@example.com";
        when(jpaPassengerRepository.findByEmail(email)).thenReturn(Optional.of(new PassengerEntity()));

        boolean exists = passengerRepository.existsByEmail(email);

        assertTrue(exists);
        verify(jpaPassengerRepository).findByEmail(email);
    }

    @Test
    void shouldReturnTrue_whenPassengerExistsById() {
        Long id = 1L;
        when(jpaPassengerRepository.existsById(id)).thenReturn(true);

        boolean exists = passengerRepository.existsById(id);

        assertTrue(exists);
        verify(jpaPassengerRepository).existsById(id);
    }

    @Test
    void shouldReturnPassenger_whenPassengerIsFoundById() {
        Long id = 1L;
        PassengerEntity entity = new PassengerEntity();
        when(jpaPassengerRepository.findById(id)).thenReturn(Optional.of(entity));

        Optional<Passenger> result = passengerRepository.findById(id);

        assertTrue(result.isPresent());
        verify(jpaPassengerRepository).findById(id);
    }

    @Test
    void shouldReturnPassengerCount_whenRequested() {
        when(jpaPassengerRepository.count()).thenReturn(5L);

        Long count = passengerRepository.count();

        assertEquals(5L, count);
        verify(jpaPassengerRepository).count();
    }

    @Test
    void shouldReturnPassenger_whenPassengerIsFoundByEmail() {
        String email = "test@example.com";
        PassengerEntity entity = new PassengerEntity();
        when(jpaPassengerRepository.findByEmail(email)).thenReturn(Optional.of(entity));

        Optional<Passenger> result = passengerRepository.findByEmail(email);

        assertTrue(result.isPresent());
        verify(jpaPassengerRepository).findByEmail(email);
    }
}

package com.gtu.users_management_service.application.mapper;

import com.gtu.users_management_service.application.dto.PassengerDTO;
import com.gtu.users_management_service.domain.model.Passenger;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PassengerMapperTest {

    @Test
    void toDomain_shouldMapDtoToDomainWhenNotNull() {
        PassengerDTO dto = new PassengerDTO();
        dto.setId(1L);
        dto.setName("Carlos Pérez");
        dto.setEmail("carlos.perez@gtu.com");
        dto.setPassword("Passw0rd");

        Passenger passenger = PassengerMapper.toDomain(dto);

        assertNotNull(passenger);
        assertEquals(1L, passenger.getId());
        assertEquals("Carlos Pérez", passenger.getName());
        assertEquals("carlos.perez@gtu.com", passenger.getEmail());
        assertEquals("Passw0rd", passenger.getPassword());
    }

    @Test
    void toDomain_shouldReturnNullWhenDtoIsNull() {
        Passenger passenger = PassengerMapper.toDomain(null);
        assertNull(passenger);
    }

    @Test
    void toDTO_shouldMapDomainToDtoWhenNotNull() {
        Passenger passenger = new Passenger(1L, "Carlos Pérez", "carlos.perez@gtu.com", "Passw0rd");

        PassengerDTO dto = PassengerMapper.toDTO(passenger);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Carlos Pérez", dto.getName());
        assertEquals("carlos.perez@gtu.com", dto.getEmail());
        assertEquals("Passw0rd", dto.getPassword());
    }

    @Test
    void toDTO_shouldReturnNullWhenDomainIsNull() {
        PassengerDTO dto = PassengerMapper.toDTO(null);
        assertNull(dto);
    }

    @Test
    void toDTOList_shouldMapListWhenNotNull() {
        Passenger passenger1 = new Passenger(1L, "Carlos Pérez", "carlos.perez@gtu.com", "Passw0rd");
        Passenger passenger2 = new Passenger(2L, "Maria Gómez", "maria.gomez@gtu.com", "Passw0rd2");
        List<Passenger> passengers = List.of(passenger1, passenger2);

        List<PassengerDTO> dtos = PassengerMapper.toDTOList(passengers);

        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals("Carlos Pérez", dtos.get(0).getName());
        assertEquals("Maria Gómez", dtos.get(1).getName());
    }

    @Test
    void toDTOList_shouldReturnEmptyListWhenDomainListIsNull() {
        List<PassengerDTO> dtos = PassengerMapper.toDTOList(null);
        assertNotNull(dtos);
        assertTrue(dtos.isEmpty());
    }

    @Test
    void toDomainList_shouldMapListWhenNotNull() {
        PassengerDTO dto1 = new PassengerDTO();
        dto1.setId(1L);
        dto1.setName("Carlos Pérez");
        dto1.setEmail("carlos.perez@gtu.com");
        dto1.setPassword("Passw0rd");
        PassengerDTO dto2 = new PassengerDTO();
        dto2.setId(2L);
        dto2.setName("Maria Gómez");
        dto2.setEmail("maria.gomez@gtu.com");
        dto2.setPassword("Passw0rd2");
        List<PassengerDTO> dtos = List.of(dto1, dto2);

        List<Passenger> passengers = PassengerMapper.toDomainList(dtos);

        assertNotNull(passengers);
        assertEquals(2, passengers.size());
        assertEquals("Carlos Pérez", passengers.get(0).getName());
        assertEquals("Maria Gómez", passengers.get(1).getName());
    }

    @Test
    void toDomainList_shouldReturnEmptyListWhenDtoListIsNull() {
        List<Passenger> passengers = PassengerMapper.toDomainList(null);
        assertNotNull(passengers);
        assertTrue(passengers.isEmpty());
    }
}
package com.gtu.users_management_service.application.mapper;

import com.gtu.users_management_service.application.dto.UserDTO;
import com.gtu.users_management_service.domain.model.Role;
import com.gtu.users_management_service.domain.model.Status;
import com.gtu.users_management_service.domain.model.User;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    @Test
    void toDomain_shouldMapDtoToDomainWhenNotNull() {
        UserDTO dto = new UserDTO();
        dto.setId(1L);
        dto.setName("Carlos Pérez");
        dto.setEmail("carlos.perez@gtu.com");
        dto.setPassword("Passw0rd");
        dto.setRole(Role.ADMIN);
        dto.setStatus(Status.ACTIVE);

        User user = UserMapper.toDomain(dto);

        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("Carlos Pérez", user.getName());
        assertEquals("carlos.perez@gtu.com", user.getEmail());
        assertEquals("Passw0rd", user.getPassword());
        assertEquals(Role.ADMIN, user.getRole());
        assertEquals(Status.ACTIVE, user.getStatus());
    }

    @Test
    void toDomain_shouldReturnNullWhenDtoIsNull() {
        User user = UserMapper.toDomain(null);
        assertNull(user);
    }

    @Test
    void toDTO_shouldMapDomainToDtoWhenNotNull() {
        User user = new User();
        user.setId(1L);
        user.setName("Carlos Pérez");
        user.setEmail("carlos.perez@gtu.com");
        user.setPassword("Passw0rd");
        user.setRole(Role.ADMIN);
        user.setStatus(Status.ACTIVE);

        UserDTO dto = UserMapper.toDTO(user);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Carlos Pérez", dto.getName());
        assertEquals("carlos.perez@gtu.com", dto.getEmail());
        assertEquals("Passw0rd", dto.getPassword());
        assertEquals(Role.ADMIN, dto.getRole());
        assertEquals(Status.ACTIVE, dto.getStatus());
    }

    @Test
    void toDTO_shouldReturnNullWhenDomainIsNull() {
        UserDTO dto = UserMapper.toDTO(null);
        assertNull(dto);
    }

    @Test
    void toDTOList_shouldMapListWhenNotNull() {
        User user1 = new User(1L, "Carlos Pérez", "carlos.perez@gtu.com", "Passw0rd", Role.ADMIN, Status.ACTIVE);
        User user2 = new User(2L, "Maria Gómez", "maria.gomez@gtu.com", "Passw0rd2", Role.DRIVER, Status.INACTIVE);
        List<User> users = List.of(user1, user2);

        List<UserDTO> dtos = UserMapper.toDTOList(users);

        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals("Carlos Pérez", dtos.get(0).getName());
        assertEquals("Maria Gómez", dtos.get(1).getName());
    }

    @Test
    void toDTOList_shouldReturnEmptyListWhenDomainListIsNull() {
        List<UserDTO> dtos = UserMapper.toDTOList(null);
        assertNotNull(dtos);
        assertTrue(dtos.isEmpty());
    }

    @Test
    void toDomainList_shouldMapListWhenNotNull() {
        UserDTO dto1 = new UserDTO();
        dto1.setId(1L);
        dto1.setName("Carlos Pérez");
        dto1.setEmail("carlos.perez@gtu.com");
        dto1.setPassword("Passw0rd");
        dto1.setRole(Role.ADMIN);
        dto1.setStatus(Status.ACTIVE);
        UserDTO dto2 = new UserDTO();
        dto2.setId(2L);
        dto2.setName("Maria Gómez");
        dto2.setEmail("maria.gomez@gtu.com");
        dto2.setPassword("Passw0rd2");
        dto2.setRole(Role.DRIVER);
        dto2.setStatus(Status.INACTIVE);
        List<UserDTO> dtos = List.of(dto1, dto2);

        List<User> users = UserMapper.toDomainList(dtos);

        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals("Carlos Pérez", users.get(0).getName());
        assertEquals("Maria Gómez", users.get(1).getName());
    }

    @Test
    void toDomainList_shouldReturnEmptyListWhenDtoListIsNull() {
        List<User> users = UserMapper.toDomainList(null);
        assertNotNull(users);
        assertTrue(users.isEmpty());
    }
}
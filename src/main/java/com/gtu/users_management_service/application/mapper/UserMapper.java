package com.gtu.users_management_service.application.mapper;

import java.util.List;

import com.gtu.users_management_service.application.dto.UserDTO;
import com.gtu.users_management_service.domain.model.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {

    public User toDomain(UserDTO dto) {
        if (dto == null) {
            return null;
        }
        return new User(
                dto.getId(),
                dto.getName(),
                dto.getEmail(),
                dto.getPassword(),
                dto.getRole(),
                dto.getStatus()
        );
    }

    public UserDTO toDTO(User domain) {
        if (domain == null) {
            return null;
        }
        UserDTO dto = new UserDTO();
        dto.setId(domain.getId());
        dto.setName(domain.getName());
        dto.setEmail(domain.getEmail());
        dto.setPassword(domain.getPassword());
        dto.setRole(domain.getRole());
        dto.setStatus(domain.getStatus());
        return dto;
    }

    public static List<UserDTO> toDTOList(List<User> domainList) {
        return domainList == null ? List.of() : domainList.stream()
                .map(UserMapper::toDTO).toList();
    }

    public static List<User> toDomainList(List<UserDTO> dtoList) {
        return dtoList == null ? List.of() : dtoList.stream()
                .map(UserMapper::toDomain).toList();
    }
}
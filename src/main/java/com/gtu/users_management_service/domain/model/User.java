package com.gtu.users_management_service.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {

    private Long id;

    private String name;

    private String email;

    private String password;

    private Role role;

    private Status status;

    public User() {
    }

    public User(Long id, String name, String email, String password, Role role, Status status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = status;
    }
}
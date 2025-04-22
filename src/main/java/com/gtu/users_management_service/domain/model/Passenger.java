package com.gtu.users_management_service.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Passenger {
   
    private Long id;
    
    private String name;

    private String email;

    private String password;

    public Passenger() {
    }

    public Passenger(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }
}

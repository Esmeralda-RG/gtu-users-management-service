package com.gtu.users_management_service.infrastructure.messaging.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreatedEvent {
    private String email;
    private String username;
    private String password;

}

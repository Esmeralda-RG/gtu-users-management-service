package com.gtu.users_management_service.infrastructure.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordValidatorTest {

    @Test
    void shouldReturnFalse_whenPasswordIsNull() {
        assertFalse(PasswordValidator.isValid(null));
    }

    @Test
    void shouldReturnFalse_whenPasswordIsTooShort() {
        assertFalse(PasswordValidator.isValid("A1b")); // menos de 8 caracteres
    }

    @Test
    void shouldReturnFalse_whenPasswordLacksUppercase() {
        assertFalse(PasswordValidator.isValid("password1"));
    }

    @Test
    void shouldReturnFalse_whenPasswordLacksLowercase() {
        assertFalse(PasswordValidator.isValid("PASSWORD1"));
    }

    @Test
    void shouldReturnFalse_whenPasswordLacksDigit() {
        assertFalse(PasswordValidator.isValid("Password"));
    }

    @Test
    void shouldReturnTrue_whenPasswordMeetsAllCriteria() {
        assertTrue(PasswordValidator.isValid("Password1"));
    }
}

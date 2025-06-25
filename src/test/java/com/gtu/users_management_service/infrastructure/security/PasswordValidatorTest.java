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
        assertFalse(PasswordValidator.isValid("A1b")); 
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

    @Test
    void shouldReturnTrue_whenPasswordIsExactlyMinLength() {
        assertTrue(PasswordValidator.isValid("A1bcdefg")); 
    }

    @Test
    void shouldReturnTrue_whenPasswordIsLongerThanMinLength() {
        assertTrue(PasswordValidator.isValid("A1bcdefghijklmnop"));
    }

    @Test
    void shouldReturnTrue_whenPasswordContainsSpecialCharacters() {
        assertTrue(PasswordValidator.isValid("A1b@cdef"));
    }

    @Test
    void shouldReturnFalse_whenPasswordLacksUppercaseAndDigit() {
        assertFalse(PasswordValidator.isValid("password"));
    }

    @Test
    void shouldReturnFalse_whenPasswordLacksLowercaseAndDigit() {
        assertFalse(PasswordValidator.isValid("PASSWORD"));
    }
}

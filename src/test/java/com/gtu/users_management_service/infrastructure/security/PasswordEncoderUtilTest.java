package com.gtu.users_management_service.infrastructure.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordEncoderUtilTest {

    @Test
    void shouldEncodePasswordSuccessfully() {
        String rawPassword = "Password123";
        String encoded = PasswordEncoderUtil.encode(rawPassword);

        assertNotNull(encoded);
        assertNotEquals(rawPassword, encoded); 
    }

    @Test
    void shouldMatchEncodedPasswordSuccessfully() {
        String rawPassword = "Password123";
        String encoded = PasswordEncoderUtil.encode(rawPassword);

        assertTrue(PasswordEncoderUtil.matches(rawPassword, encoded));
    }

    @Test
    void shouldFailWhenPasswordsDoNotMatch() {
        String rawPassword = "Password123";
        String encoded = PasswordEncoderUtil.encode("OtherPassword");

        assertFalse(PasswordEncoderUtil.matches(rawPassword, encoded));
    }

    @Test
    void shouldEncodeEmptyPassword() {
        String encoded = PasswordEncoderUtil.encode("");
        assertNotNull(encoded);
        assertFalse(encoded.isEmpty());
    }

    @Test
    void shouldGenerateDifferentHashesForSamePassword() {
        String rawPassword = "Password123";
        String encoded1 = PasswordEncoderUtil.encode(rawPassword);
        String encoded2 = PasswordEncoderUtil.encode(rawPassword);

        assertNotEquals(encoded1, encoded2); 
    }
}

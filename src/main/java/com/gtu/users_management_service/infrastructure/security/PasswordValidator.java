package com.gtu.users_management_service.infrastructure.security;

import java.util.regex.Pattern;

public class PasswordValidator {

    private static final int MIN_LENGTH = 8;

    public static boolean isValid(String password) {
        if (password == null || password.length() < MIN_LENGTH) return false;

        boolean hasUpper = Pattern.compile("[A-Z]").matcher(password).find();
        boolean hasLower = Pattern.compile("[a-z]").matcher(password).find();
        boolean hasDigit = Pattern.compile("[0-9]").matcher(password).find();

        return hasUpper && hasLower && hasDigit;
    }
}
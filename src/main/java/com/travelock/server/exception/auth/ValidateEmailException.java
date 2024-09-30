package com.travelock.server.exception.auth;


import com.travelock.server.exception.base_exceptions.ValidationException;

public class ValidateEmailException extends ValidationException {
    public ValidateEmailException(String message) {
        super(message);
    }
}

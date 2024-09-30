package com.travelock.server.exception.auth;


import com.travelock.server.exception.base_exceptions.ValidationException;

public class ValidateNickNameException extends ValidationException {
    public ValidateNickNameException(String message) {
        super(message);
    }
}

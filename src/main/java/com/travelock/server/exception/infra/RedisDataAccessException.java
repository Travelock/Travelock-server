package com.travelock.server.exception.infra;


import com.travelock.server.exception.base_exceptions.BadRequestException;

public class RedisDataAccessException extends BadRequestException {
    public RedisDataAccessException(String message) {
        super(message);
    }
}

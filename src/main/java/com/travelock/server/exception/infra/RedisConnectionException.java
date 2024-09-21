package com.travelock.server.exception.infra;


import com.travelock.server.exception.base_exceptions.ServiceUnavailableException;

public class RedisConnectionException extends ServiceUnavailableException {
    public RedisConnectionException(String message) {
        super(message);
    }
}

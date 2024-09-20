package com.travelock.server.exception.course;


import com.travelock.server.exception.base_exceptions.BadRequestException;

public class AddFullCourseFavoriteException extends BadRequestException {
    public AddFullCourseFavoriteException(String message) {
        super(message);
    }
}

package com.travelock.server.exception.course;


import com.travelock.server.exception.base_exceptions.BadRequestException;

public class AddDailyCourseFavoriteException extends BadRequestException {
    public AddDailyCourseFavoriteException(String message) {
        super(message);
    }
}

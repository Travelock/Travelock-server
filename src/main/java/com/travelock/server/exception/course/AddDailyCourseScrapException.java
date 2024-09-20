package com.travelock.server.exception.course;


import com.travelock.server.exception.base_exceptions.BadRequestException;

public class AddDailyCourseScrapException extends BadRequestException {
    public AddDailyCourseScrapException(String message) {
        super(message);
    }
}

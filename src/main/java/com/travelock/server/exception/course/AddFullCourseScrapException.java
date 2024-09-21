package com.travelock.server.exception.course;


import com.travelock.server.exception.base_exceptions.BadRequestException;

public class AddFullCourseScrapException extends BadRequestException {
    public AddFullCourseScrapException(String message) {
        super(message);
    }
}

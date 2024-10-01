package com.travelock.server.exception.course;

import com.travelock.server.exception.base_exceptions.BadRequestException;

public class EmptyTitleException extends BadRequestException {
    public EmptyTitleException(String message) {super(message);}
}

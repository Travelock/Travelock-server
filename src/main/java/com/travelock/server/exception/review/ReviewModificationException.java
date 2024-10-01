package com.travelock.server.exception.review;

import com.travelock.server.exception.base_exceptions.BadRequestException;

public class ReviewModificationException extends BadRequestException {
    public ReviewModificationException(String message) {
        super(message);
    }
}

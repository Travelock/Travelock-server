package com.travelock.server.exception.review;

import com.travelock.server.exception.base_exceptions.BadRequestException;

public class AddReviewException extends BadRequestException {
    public AddReviewException(String message) {
        super(message);
    }


}

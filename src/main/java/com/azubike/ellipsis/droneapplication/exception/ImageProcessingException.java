package com.azubike.ellipsis.droneapplication.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class ImageProcessingException extends RuntimeException {
    public ImageProcessingException(final String message) {
        super(message);
    }
    public ImageProcessingException(final String message, final Throwable cause) {
        super(message, cause);
    }
}

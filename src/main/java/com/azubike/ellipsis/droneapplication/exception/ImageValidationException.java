package com.azubike.ellipsis.droneapplication.exception;

public class ImageValidationException extends  RuntimeException{
    public ImageValidationException(final String message) {
        super(message);
    }

    public ImageValidationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}

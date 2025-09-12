package org.karunamay.core.exception;

public class RoutesNotFoundException extends RuntimeException {

    public RoutesNotFoundException(String message) {
        throw new RuntimeException(message);
    }

    public RoutesNotFoundException(String message, Throwable cause) {
        throw new RuntimeException(message, cause);
    }
}

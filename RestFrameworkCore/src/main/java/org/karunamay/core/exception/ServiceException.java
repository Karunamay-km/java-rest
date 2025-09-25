package org.karunamay.core.exception;

public class ServiceException extends RuntimeException {
    public ServiceException(String message, Throwable e) {
        super(message, e);
    }
}

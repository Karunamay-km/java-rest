package org.karunamay.core.exception;

public class DuplicateObjectException extends RuntimeException {
    public DuplicateObjectException(String message, Throwable e) {
        super(message, e);
    }
}

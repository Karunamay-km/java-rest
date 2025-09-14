package org.karunamay.core.exception;

public class ApplicationContextException extends RuntimeException {
    public ApplicationContextException(Exception e) {
        super(e.getMessage());
        e.printStackTrace();
        Throwable cause = e.getCause();
        while (cause != null) {
            cause.printStackTrace();
            cause = cause.getCause();
        }
    }
}

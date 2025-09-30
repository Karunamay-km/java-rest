package org.karunamay.core.exception;

public class ResponseSentException extends RuntimeException {
    public ResponseSentException() {
        super("Response has been sent to the client. Stop further execution");
    }
}

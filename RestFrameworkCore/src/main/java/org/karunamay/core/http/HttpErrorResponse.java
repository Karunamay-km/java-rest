package org.karunamay.core.http;

public class HttpErrorResponse<T> {

    private final int code;
    private final String message;
    private final T details;

    public HttpErrorResponse(int code, String message, T details) {
        this.code = code;
        this.message = message;
        this.details = details;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getDetails() {
        return details;
    }
}

package org.karunamay.core.http;

public class HttpErrorResponse<T> extends RestHttpResponse<T> {

    public HttpErrorResponse(int code, String message, T details) {
        super(code, message, details);
    }
}

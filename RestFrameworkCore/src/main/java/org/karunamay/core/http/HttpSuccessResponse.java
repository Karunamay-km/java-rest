package org.karunamay.core.http;

public class HttpSuccessResponse<T> extends RestHttpResponse<T> {

    public HttpSuccessResponse(int code, String message, T details) {
        super(code, message, details);
    }
}

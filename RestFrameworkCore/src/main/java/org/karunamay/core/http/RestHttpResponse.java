package org.karunamay.core.http;

import lombok.Getter;

@Getter
public class RestHttpResponse<T> {

    private final int code;
    private final String message;
    private final T details;

    public RestHttpResponse(int code, String message, T details) {
        this.code = code;
        this.message = message;
        this.details = details;
    }
}

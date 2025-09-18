package org.karunamay.core.http;

import org.karunamay.core.api.http.HttpStatus;

public class HttpErrorResponseJson <T> extends BaseHttpResponseJson {

    private HttpErrorResponse<T> error;

    public HttpErrorResponseJson(HttpErrorResponse<T> error) {
        this.error = error;
    }
}

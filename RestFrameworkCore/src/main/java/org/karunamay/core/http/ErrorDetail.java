package org.karunamay.core.http;

public class ErrorDetail<T> extends BaseHttpResponseJson {

    private HttpErrorResponse<T> error;

    public ErrorDetail(HttpErrorResponse<T> error) {
        this.error = error;
    }
}

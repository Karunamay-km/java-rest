package org.karunamay.core.api.http;

public enum HttpStatus {
    HTTP_OK(200, "OK"),
    HTTP_NOT_FOUND(404, "Not Found"),
    HTTP_BAD_REQUEST(400, "Bad Request"),
    HTTP_SERVER_ERROR(500, "Server Error"),
    HTTP_AUTHENTICATION_SUCCESS(204, "Authentication Success"),
    HTTP_CREATE(201, "Created");

    final int code;
    final String responsePhrase;

    HttpStatus(int code, String responsePhrase) {
        this.code = code;
        this.responsePhrase = responsePhrase;
    }

    public int getCode() {
        return code;
    }

    public String getResponsePhrase() {
        return responsePhrase;
    }
}

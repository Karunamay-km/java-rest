package org.karunamay.core.internal;


import org.karunamay.core.api.http.HttpContext;
import org.karunamay.core.api.http.HttpRequest;

import java.io.OutputStream;


class HttpContextImpl implements HttpContext {

    private final HttpRequest request;
    private final OutputStream outputStream;

    HttpContextImpl(HttpRequest request, OutputStream outputStream) {
        this.request = request;
        this.outputStream = outputStream;
    }

    public HttpRequest getRequest() {
        return request;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }
}

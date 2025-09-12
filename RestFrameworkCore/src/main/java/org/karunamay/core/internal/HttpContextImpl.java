package org.karunamay.core.internal;


import org.karunamay.core.api.http.HttpContext;
import org.karunamay.core.api.http.HttpHeader;
import org.karunamay.core.api.http.HttpRequest;
import org.karunamay.core.http.HttpHeaderFactory;

import java.io.OutputStream;


class HttpContextImpl implements HttpContext {

    private final HttpRequest request;
    private final OutputStream outputStream;
    private final HttpHeader responseHeader;

    HttpContextImpl(HttpRequest request, OutputStream outputStream) {
        this.request = request;
        this.outputStream = outputStream;
        this.responseHeader = HttpHeaderFactory.create();
    }

    @Override
    public HttpRequest getRequest() {
        return this.request;
    }

    @Override
    public OutputStream getOutputStream() {
        return this.outputStream;
    }

    @Override
    public HttpHeader getResponseHeader() {
        return this.responseHeader;
    }
}

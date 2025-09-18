package org.karunamay.core.internal;


import org.karunamay.core.api.http.ApplicationContext;
import org.karunamay.core.api.http.HttpHeader;
import org.karunamay.core.api.http.HttpRequest;
import org.karunamay.core.api.router.PathParameter;

import java.io.OutputStream;
import java.util.Map;


class ApplicationContextImpl implements ApplicationContext {

    private static final ApplicationContextImpl INSTANCE = new ApplicationContextImpl();

    private final ThreadLocal<HttpRequest> httpRequest = new ThreadLocal<>();
    private final ThreadLocal<OutputStream> outputStream = new ThreadLocal<>();
    private final ThreadLocal<HttpHeader> responseHeader = new ThreadLocal<>();
    private final ThreadLocal<Map<String, PathParameter>> pathParameters = new ThreadLocal<>();

    public ApplicationContextImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public void setPathParameter(Map<String, PathParameter> pathParameter) {
        pathParameters.set(pathParameter);
    }

    @Override
    public HttpRequest getRequest() {
        return httpRequest.get();
    }

    @Override
    public void setHttpRequest(HttpRequest request) {
        this.httpRequest.set(request);
    }

    @Override
    public OutputStream getOutputStream() {
        return outputStream.get();
    }

    @Override
    public void setOutputStream(OutputStream outputStream) {
        this.outputStream.set(outputStream);
    }

    @Override
    public HttpHeader getResponseHeader() {
        return responseHeader.get();
    }

    @Override
    public void setResponseHeader(HttpHeader responseHeader) {
        this.responseHeader.set(responseHeader);
    }

    @Override
    public Map<String, PathParameter> getPathParameters() {
        return pathParameters.get();
    }

}
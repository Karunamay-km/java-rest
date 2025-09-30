package org.karunamay.core.internal;


import org.karunamay.core.api.config.ConfigManager;
import org.karunamay.core.api.controller.RestControllerConfig;
import org.karunamay.core.api.http.ApplicationContext;
import org.karunamay.core.api.http.HttpHeader;
import org.karunamay.core.api.http.HttpRequest;
import org.karunamay.core.api.router.PathParameter;
import org.karunamay.core.api.middleware.Middleware;
import org.karunamay.core.api.router.RouteComponent;
import org.karunamay.core.http.HttpHeaderFactory;
//import org.karunamay.core.router.RouteComponent;

import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class ApplicationContextImpl implements ApplicationContext {

    private HttpRequest httpRequest;
    private OutputStream outputStream;
    private HttpHeader responseHeader;
    private Map<String, PathParameter> pathParameters;
    private ConfigManager configManager;
    private RouteComponent route;
    private final LinkedList<Middleware> requestMiddlewares = new LinkedList<>();
    private final LinkedList<Middleware> responseMiddlewares = new LinkedList<>();
    private boolean responseWritten = false;
    private boolean earlyTermination = false;

    public ApplicationContextImpl() {
    }

    public ApplicationContextImpl(HttpRequest request,
                                  OutputStream outputStream,
                                  HttpHeader responseHeader,
                                  Map<String, PathParameter> pathParameters,
                                  ConfigManager configManager
    ) {
        httpRequest = request;
        this.outputStream = outputStream;
        this.responseHeader = responseHeader;
        this.pathParameters = pathParameters;
        this.configManager = configManager;
    }

    @Override
    public RouteComponent getRoute() {
        return route;
    }

    @Override
    public void setRoute(RouteComponent route) {
        this.route = route;
    }

    @Override
    public boolean isEarlyTermination() {
        return earlyTermination;
    }

    @Override
    public void setEarlyTermination(boolean earlyTermination) {
        this.earlyTermination = earlyTermination;
    }

    @Override
    public boolean isResponseWritten() {
        return this.responseWritten;
    }

    @Override
    public void setResponseWritten(boolean responseWritten) {
        this.responseWritten = responseWritten;
    }

    @Override
    public void setRequestMiddlewares(List<Middleware> requestMiddlewares) {
        this.requestMiddlewares.addAll(requestMiddlewares);
    }

    @Override
    public void setResponseMiddlewares(List<Middleware> responseMiddlewares) {
        this.responseMiddlewares.addAll(responseMiddlewares);
    }

    @Override
    public LinkedList<Middleware> getRequestMiddlewares() {
        return this.requestMiddlewares;
    }

    @Override
    public LinkedList<Middleware> getResponseMiddlewares() {
        return this.responseMiddlewares;
    }

    @Override
    public void setPathParameter(Map<String, PathParameter> pathParameter) {
        this.pathParameters = pathParameter;
    }


    @Override
    public OutputStream getOutputStream() {
        return this.outputStream;
    }

    @Override
    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public HttpHeader getResponseHeader() {
        return this.responseHeader;
    }

    @Override
    public void setResponseHeader(HttpHeader responseHeader) {
        HttpHeader headers = HttpHeaderFactory.create();
        responseHeader.asMap().forEach(headers::set);
        this.responseHeader = headers;
    }

    @Override
    public Map<String, PathParameter> getPathParameters() {
        return this.pathParameters;
    }

    @Override
    public HttpRequest getHttpRequest() {
        return this.httpRequest;
    }

    @Override
    public void setHttpRequest(HttpRequest request) {
        this.httpRequest = request;
    }

    @Override
    public void setPathParameters(Map<String, PathParameter> pathParameters) {
        this.pathParameters = pathParameters;
    }

    @Override
    public ConfigManager getConfigManager() {
        return configManager;
    }

    @Override
    public void setConfigManager(ConfigManager configManager) {
        this.configManager = configManager;
    }
}
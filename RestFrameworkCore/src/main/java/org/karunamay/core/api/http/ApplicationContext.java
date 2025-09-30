package org.karunamay.core.api.http;

import org.karunamay.core.api.config.ConfigManager;
import org.karunamay.core.api.controller.RestControllerConfig;
import org.karunamay.core.api.router.PathParameter;
import org.karunamay.core.api.middleware.Middleware;
import org.karunamay.core.api.router.RouteComponent;
import org.karunamay.core.router.Route;

import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public interface ApplicationContext {

    void setHttpRequest(HttpRequest httpRequest);

    OutputStream getOutputStream();

    void setOutputStream(OutputStream outputStream);

    HttpHeader getResponseHeader();

    void setResponseHeader(HttpHeader responseHeader);

    Map<String, PathParameter> getPathParameters();

    boolean isEarlyTermination();

    void setEarlyTermination(boolean earlyTermination);

    boolean isResponseWritten();

    void setResponseWritten(boolean responseWritten);

    void setRequestMiddlewares(List<Middleware> requestMiddlewares);

    void setResponseMiddlewares(List<Middleware> requestMiddlewares);

    LinkedList<Middleware> getRequestMiddlewares();

    LinkedList<Middleware> getResponseMiddlewares();

    void setPathParameter(Map<String, PathParameter> pathParameter);

    HttpRequest getHttpRequest();

    void setPathParameters(Map<String, PathParameter> pathParameters);

    ConfigManager getConfigManager();

    void setConfigManager(ConfigManager configManager);

    RouteComponent getRoute();

    void setRoute(RouteComponent route);
}

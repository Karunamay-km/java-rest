package org.karunamay.core.api.router;

import org.karunamay.core.api.controller.RestControllerConfig;

import java.util.Map;

public interface RouteComponent<T extends RestControllerConfig> {
    String getRawPath();

    Class<T> getController();

    String getName();

    Map<String, PathParameter> getPathParameters();

    void setRawPath(String path);

    void setPathParameters(String path, PathParameter pathParameter);
}

package org.karunamay.core.api.router;

import org.karunamay.core.api.controller.RestControllerConfig;

import java.util.Map;

public interface RouteComponent {
    String getRawPath();

    Class<? extends RestControllerConfig> getController();

    String getName();

    Map<String, PathParameter> getPathParameters();

    void setRawPath(String path);

    void setPathParameters(String path, PathParameter pathParameter);

    Boolean isPublicRoute();
}

package org.karunamay.core.router;

import org.karunamay.core.api.controller.RestControllerConfig;
import org.karunamay.core.api.router.PathParameter;
import org.karunamay.core.api.router.RouteComponent;

import java.util.HashMap;
import java.util.Map;

public class Route implements RouteComponent {

    private String rawPath;
    private final Class<? extends RestControllerConfig> controller;
    private final String name;
    private Boolean isPublicRoute = false;
    private final Map<String, PathParameter> pathParameters = new HashMap<>();

    public Route(String rawPath, Class<? extends RestControllerConfig> controller, String name) {
        this.rawPath = rawPath;
        this.controller = controller;
        this.name = name;
    }

    public Route(String rawPath, Class<? extends RestControllerConfig> controller, String name, Boolean isPublicRoute) {
        this.rawPath = rawPath;
        this.controller = controller;
        this.name = name;
        this.isPublicRoute = isPublicRoute;
    }

    @Override
    public String getRawPath() {
        return this.rawPath;
    }

    @Override
    public Class<? extends RestControllerConfig> getController() {
        return this.controller;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Map<String, PathParameter> getPathParameters() {
        return pathParameters;
    }

    @Override
    public void setRawPath(String path) {
        this.rawPath = path;
    }

    @Override
    public void setPathParameters(String path, PathParameter pathParameter) {
        this.pathParameters.put(path, pathParameter);
    }

    @Override
    public Boolean isPublicRoute() {
        return this.isPublicRoute;
    }
}

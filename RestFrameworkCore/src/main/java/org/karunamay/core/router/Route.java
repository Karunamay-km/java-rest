package org.karunamay.core.router;

import org.karunamay.core.api.controller.RestControllerConfig;
import org.karunamay.core.api.router.PathParameter;
import org.karunamay.core.api.router.RouteComponent;

import java.util.HashMap;
import java.util.Map;

public class Route<T extends RestControllerConfig> implements RouteComponent<T> {

    private String rawPath;
    private final Class<T> controller;
    //    private final AbstractPathParams params = new PathParams();
    private final String name;
    private final Map<String, PathParameter> pathParameters = new HashMap<>();
//    private boolean hasPathParameters = false;

    public Route(String rawPath, Class<T> controller, String name) {
        this.rawPath = rawPath;
        this.controller = controller;
        this.name = name;
    }

    @Override
    public String getRawPath() {
        return this.rawPath;
    }

    @Override
    public Class<T> getController() {
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

}

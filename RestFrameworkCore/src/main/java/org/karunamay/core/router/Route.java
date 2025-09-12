package org.karunamay.core.router;

import org.karunamay.core.api.controller.RestControllerConfig;

public class Route<T extends RestControllerConfig> {

    private final String path;
    private final Class<T> controller;
    private final AbstractPathParams params = new PathParams();

    public Route(String path, Class<T> controller) {
        this.path = path;
        this.controller = controller;
    }

    public String getPath() {
        return path;
    }

    public Class<T> getController() {
        return controller;
    }

    public AbstractPathParams getPathParams() {
        return this.params;
    }
}

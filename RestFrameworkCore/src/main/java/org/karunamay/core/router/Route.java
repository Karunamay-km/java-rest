package org.karunamay.core.router;

import org.karunamay.core.api.controller.RestControllerConfig;
import org.karunamay.core.api.router.RouteComponent;

public class Route <T extends RestControllerConfig> implements RouteComponent<T> {

    private final String path;
    private final Class<T> controller;
    private final AbstractPathParams params = new PathParams();
    private final String name;

    public Route(String path, Class<T> controller, String name) {
        this.path = path;
        this.controller = controller;
        this.name = name;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public Class<T> getController() {
        return this.controller;
    }

    @Override
    public String getName() {
        return name;
    }

    //    @Override
//    public AbstractPathParams getPathParams() {
//        return this.params;
//    }
}

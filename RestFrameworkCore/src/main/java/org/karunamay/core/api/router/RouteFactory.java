package org.karunamay.core.api.router;

import org.karunamay.core.api.controller.RestControllerConfig;
import org.karunamay.core.router.Route;

public class RouteFactory {

    public static RouteComponent create(
            String path, Class<? extends RestControllerConfig> controller, String name
    ) {
        return new Route(path, controller, name);
    }

    public static RouteComponent create(
            String path, Class<? extends RestControllerConfig> controller, String name, Boolean isPublicRoute
    ) {
        return new Route(path, controller, name, isPublicRoute);
    }
}

package org.karunamay.core.api.router;

import org.karunamay.core.api.controller.RestControllerConfig;
import org.karunamay.core.router.Route;

public class RouteFactory {

    public static <T extends RestControllerConfig> RouteComponent<T> create(
            String path, Class<T> controller, String name
    ) {
        return new Route<>(path, controller, name);
    }
}

package org.karunamay.core.api.router;

import org.karunamay.core.api.controller.RestControllerConfig;
import org.karunamay.core.router.Route;

import java.util.Map;

public interface RouteRegistry {

    Map<String, Route<?>> getRoutes();

    <T extends RestControllerConfig> void register(String path, Class<T> controller, String name);

    Route<?> getRoute(String key);
}

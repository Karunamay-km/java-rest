package org.karunamay.core.internal;

import org.karunamay.core.api.controller.RestControllerConfig;
import org.karunamay.core.api.router.RouteRegistry;
import org.karunamay.core.router.Route;

import java.util.HashMap;
import java.util.Map;

class RouteRegistryImpl implements RouteRegistry {

    private static final RouteRegistry INSTANCE = new RouteRegistryImpl();
    private final Map<String, Route<?>> routes;

    RouteRegistryImpl() {
        this.routes = new HashMap<>();
    }

    public static RouteRegistry getInstance() {
        return INSTANCE;
    }

    public Map<String, Route<?>> getRoutes() {
        return this.routes;
    }

    public Route<?> getRoute(String key) {
        return this.routes.get(key);
    }

    public <T extends RestControllerConfig> void register(String path, Class<T> controller, String name) {
        this.routes.put(path, new Route<>(path, controller));
    }
}

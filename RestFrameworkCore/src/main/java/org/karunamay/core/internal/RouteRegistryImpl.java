package org.karunamay.core.internal;

import org.karunamay.core.api.router.RouteComponent;
import org.karunamay.core.api.router.RouteRegistry;
import org.karunamay.core.api.router.RouterConfig;
import org.karunamay.core.exception.NoRouteFoundException;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Supplier;


public class RouteRegistryImpl implements RouteRegistry {

    private static final RouteRegistry INSTANCE = new RouteRegistryImpl();
    private final Map<String, RouteComponent> routes = new HashMap<>();
    private final Map<String, String> routesNameMapper = new HashMap<>();
    private final ConcurrentLinkedQueue<Pair<String, List<RouteComponent>>> routeLoaderQueue =
            new ConcurrentLinkedQueue<>();

    RouteRegistryImpl() {
    }

    public static RouteRegistry getInstance() {
        return INSTANCE;
    }

    @Override
    public Map<String, RouteComponent> getRoutes() {
        return this.routes;
    }

    @Override
    public Map<String, String> getRoutesNameMapper() {
        return routesNameMapper;
    }

    @Override
    public void add(
            String parentRouteName, Supplier<List<RouteComponent>> routes
    ) {
        String parentRoute = routesNameMapper.get(parentRouteName);
        if (parentRoute == null) {
            routeLoaderQueue.add(new Pair<>(parentRouteName, routes.get()));
        } else {
            for (RouteComponent route : routes.get()) {
                String path = parentRoute + route.getRawPath();
                route.setRawPath(path);
                this.register(path, route);
            }
        }
    }

    @Override
    public void add(Supplier<List<RouteComponent>> routes) {
        for (RouteComponent route : routes.get()) {
            this.register(route);
        }
    }

    @Override
    public void configureRoutes() {
        ServiceLoader<RouterConfig> loader = ServiceLoader.load(RouterConfig.class);
        for (RouterConfig config : loader) {
            config.configure(INSTANCE);
        }
        if (!routeLoaderQueue.isEmpty()) {
            for (Pair<String, List<RouteComponent>> route : routeLoaderQueue) {
                String parentRoute = routesNameMapper.get(route.key);
                if (parentRoute == null) {
                    throw new NoRouteFoundException("No such route named " + route.key + " has been found");
                }
                getInstance().add(route.key, () -> route.value);
            }
        }
    }

    private void register(RouteComponent route) {
        this.routes.put(route.getRawPath(), route);
        routesNameMapper.put(route.getName(), route.getRawPath());
    }

    private void register(String modifiedPath, RouteComponent route) {
        this.routes.put(modifiedPath, route);
        routesNameMapper.put(route.getName(), modifiedPath);
    }

    record Pair<K, V>(K key, V value) {
    }
}

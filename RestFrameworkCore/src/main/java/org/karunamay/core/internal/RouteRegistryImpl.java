package org.karunamay.core.internal;

import org.karunamay.core.api.controller.RestControllerConfig;
import org.karunamay.core.api.router.Annotation.NestedRoute;
import org.karunamay.core.api.router.RouteComponent;
import org.karunamay.core.api.router.RouteRegistry;
import org.karunamay.core.api.router.RouterConfig;
import org.karunamay.core.exception.NoRouteFoundException;
import org.karunamay.core.router.Route;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Supplier;


class RouteRegistryImpl implements RouteRegistry {

    private static final RouteRegistry INSTANCE = new RouteRegistryImpl();
    private final Map<String, RouteComponent<?>> routes;
    private static final Map<String, String> routesNameMapper = new HashMap<>();
    private static ConcurrentLinkedQueue<Pair<String, List<RouteComponent<?>>>> routeLoaderQueue =
            new ConcurrentLinkedQueue<>();


    RouteRegistryImpl() {
        this.routes = new HashMap<>();
    }

    public static RouteRegistry getInstance() {
        return INSTANCE;
    }

    @Override
    public Map<String, RouteComponent<?>> getRoutes() {
        return this.routes;
    }

    @Override
    public Map<String, String> getRoutesNameMapper() {
        return routesNameMapper;
    }

    @Override
    public <T extends RestControllerConfig> RouteComponent<T> getRoute(String key) {
        return (RouteComponent<T>) this.routes.get(key);
    }

    @Override
    public <T extends RestControllerConfig> void register(String path, Class<T> controller, String name) {
        System.out.println(this.getClass().isAnnotationPresent(NestedRoute.class));
        this.routes.put(path, new Route<>(path, controller, name));
        routesNameMapper.put(name, path);
    }

    @Override
    public <T extends RouterConfig> void include(String path, Class<T> router, String of) {

    }

    @Override
    public void add(
            String parentRouteName, Supplier<List<RouteComponent<?>>> routes
    ) {
        System.out.println(routesNameMapper);
        String parentRoute = routesNameMapper.get(parentRouteName);
        if (parentRoute == null) {
            routeLoaderQueue.add(new Pair<>(parentRouteName, routes.get()));
        } else {
            for (RouteComponent<?> route : routes.get()) {
                String path = parentRoute + route.getPath();
                this.register(path, route.getController(), route.getName());
            }
        }
    }

    public static void configureRoutes() {
        ServiceLoader<RouterConfig> loader = ServiceLoader.load(RouterConfig.class);
        System.out.println(loader);
        for (RouterConfig config : loader) {
            config.configure(INSTANCE);
        }
        // check the queue
        // if routes are in queue push them in registry.
        if (!routeLoaderQueue.isEmpty()) {
            for (Pair<String, List<RouteComponent<?>>> route : routeLoaderQueue) {
                String parentRoute = routesNameMapper.get(route.key);
                if (parentRoute == null) {
                    throw new NoRouteFoundException("No such route named " + route.key + " has been found");
                }
                new RouteRegistryImpl().add(route.key, () -> route.value);
            }
        }
    }

//    private void processRouteLoaderQueue(Pair ) {
//
//    }

    record Pair<K, V>(K key, V value) {
    }
}

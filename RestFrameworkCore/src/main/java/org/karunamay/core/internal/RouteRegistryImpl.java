package org.karunamay.core.internal;

import org.karunamay.core.api.controller.RestControllerConfig;
import org.karunamay.core.api.http.HttpRequest;
import org.karunamay.core.api.router.RouteComponent;
import org.karunamay.core.api.router.RouteRegistry;
import org.karunamay.core.api.router.RouterConfig;
import org.karunamay.core.exception.NoRouteFoundException;
import org.karunamay.core.router.PathParameterImpl;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Supplier;
import java.util.stream.Collectors;


public class RouteRegistryImpl implements RouteRegistry {

    private static final RouteRegistry INSTANCE = new RouteRegistryImpl();
    private final Map<String, RouteComponent<?>> routes;
    private static final Map<String, String> routesNameMapper = new HashMap<>();
    private static ConcurrentLinkedQueue<Pair<String, List<RouteComponent<?>>>> routeLoaderQueue =
            new ConcurrentLinkedQueue<>();

    private static ThreadLocal<HttpRequest> httpRequest = new ThreadLocal<>();

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
    public <T extends RestControllerConfig> RouteComponent<T> getRoute(String route) {
        RouteComponent<T> routeComponent = this.routes.get(route) == null
                ? requestPathToRawPathMapper()
                : (RouteComponent<T>) this.routes.get(route);
//        if (this.routes.get(route) == null) {
//            return requestPathToRawPathMapper();
//        }
//        return (RouteComponent<T>) this.routes.get(route);
        System.out.println("route component: " + routeComponent.getRawPath());
        this.parsePathParameters(routeComponent);
        return routeComponent;
    }

    @Override
    public void add(
            String parentRouteName, Supplier<List<RouteComponent<?>>> routes
    ) {
        String parentRoute = routesNameMapper.get(parentRouteName);
        if (parentRoute == null) {
            routeLoaderQueue.add(new Pair<>(parentRouteName, routes.get()));
        } else {
            for (RouteComponent<?> route : routes.get()) {
                String path = parentRoute + route.getRawPath();
                route.setRawPath(path);
                this.register(path, route);
            }
        }
    }

    @Override
    public void add(Supplier<List<RouteComponent<?>>> routes) {
        for (RouteComponent<?> route : routes.get()) {
            this.register(route);
        }
    }

    public static void setHttpRequest(HttpRequest request) {
        httpRequest.set(request);
    }

    public static void configureRoutes() {
//        httpRequest.set(request);
        ServiceLoader<RouterConfig> loader = ServiceLoader.load(RouterConfig.class);
        for (RouterConfig config : loader) {
            config.configure(INSTANCE);
        }
        if (!routeLoaderQueue.isEmpty()) {
            for (Pair<String, List<RouteComponent<?>>> route : routeLoaderQueue) {
                String parentRoute = routesNameMapper.get(route.key);
                if (parentRoute == null) {
                    throw new NoRouteFoundException("No such route named " + route.key + " has been found");
                }
                getInstance().add(route.key, () -> route.value);
            }
        }
    }

    private <T extends RestControllerConfig> void register(RouteComponent<T> route) {
//        this.parsePathParameters(route);
        this.routes.put(route.getRawPath(), route);
        routesNameMapper.put(route.getName(), route.getRawPath());
    }

    private <T extends RestControllerConfig> void register(String modifiedPath, RouteComponent<T> route) {
//        this.parsePathParameters(route);
        this.routes.put(modifiedPath, route);
        routesNameMapper.put(route.getName(), modifiedPath);
    }

    private <T extends RestControllerConfig> RouteComponent<T> requestPathToRawPathMapper() {
        String[] requestPathComponent = httpRequest.get().getPath().split("/");
        Map<String, RouteComponent<?>> filteredRoute =
                this.getRoutes()
                        .entrySet()
                        .stream()
                        .filter((e) ->
                                e.getKey().split("/").length == requestPathComponent.length)
                        .collect(Collectors.toMap(
                                Map.Entry::getKey, Map.Entry::getValue
                        ));
        List<String> routeName = new ArrayList<>();
        filteredRoute.forEach((rName, rComponent) -> {
            String[] rawPathComponent = rName.split("/");
            System.out.println(Arrays.toString(rawPathComponent));
            System.out.println(Arrays.toString(requestPathComponent));
            if (rawPathComponent.length == requestPathComponent.length) {
                for (int i = 0; i < requestPathComponent.length; i++) {
                    if (!rawPathComponent[i].equals(requestPathComponent[i])) {
                        if (!rawPathComponent[i].startsWith(":")) {
                            break;
                        }
                    }



//                    if ((rawPathComponent[i].equals(requestPathComponent[i]))
//                            || !(rawPathComponent[i].equals(requestPathComponent[i]) && rawPathComponent[i].startsWith(":"))) {
                        if (i == requestPathComponent.length - 1) {
                            routeName.add(rName);
                        }
//                    }
                }
//                routeName.add(rName);
            }
        });
        System.out.println(routeName);
        return (RouteComponent<T>) this.routes.get(routeName.get(0));
    }

    private <T extends RestControllerConfig> void parsePathParameters(RouteComponent<T> route) {
        String[] rawPathComponent = route.getRawPath().split("/");
        String[] pathComponent = httpRequest.get().getPath().split("/");
        for (int i = 0; i < rawPathComponent.length; i++) {
            if (rawPathComponent[i].contains(":")) {
                String componentName = rawPathComponent[i].substring(1);
                route.setPathParameters(componentName, new PathParameterImpl(componentName, i, pathComponent[i]));
            }
        }
    }

    record Pair<K, V>(K key, V value) {
    }
}

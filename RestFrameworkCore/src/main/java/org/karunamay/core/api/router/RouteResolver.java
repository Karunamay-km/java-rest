package org.karunamay.core.api.router;

import org.karunamay.core.Error.HttpError;
import org.karunamay.core.api.controller.RestControllerConfig;
import org.karunamay.core.api.http.ApplicationContext;
import org.karunamay.core.internal.RouteRegistryImpl;
import org.karunamay.core.router.PathParameterImpl;

import java.util.*;
import java.util.stream.Collectors;

public class RouteResolver {

    private final RouteRegistry routeRegistry = RouteRegistryImpl.getInstance();
    private final ApplicationContext applicationContext;

    public RouteResolver(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public RouteComponent resolve(String route) {
        return this.getRoutes().get(route) == null
                ? requestPathToRawPathMapper(route)
                : this.getRoutes().get(route);
    }

    private RouteComponent requestPathToRawPathMapper(String route) {
        List<String> routeName = new ArrayList<>();
        if (this.getRoutes().get(route) == null) {
            String[] requestPathComponent = applicationContext.getHttpRequest().getPath().split("/");
            Map<String, RouteComponent> filteredRoute =
                    this.getRoutes()
                            .entrySet()
                            .stream()
                            .filter((e) ->
                                    e.getKey().split("/").length == requestPathComponent.length)
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey, Map.Entry::getValue
                            ));
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
                        if (i == requestPathComponent.length - 1) {
                            routeName.add(rName);
                        }
                    }
                }
            });

            if (routeName.isEmpty()) {
                HttpError.controllerNotFound404(applicationContext);
            }
            return this.getRoutes().get(routeName.get(0));
        } else return this.getRoutes().get(route);
    }

    private void parsePathParameters(RouteComponent route) {
        String[] rawPathComponent = route.getRawPath().split("/");
        String[] pathComponent = applicationContext.getHttpRequest().getPath().split("/");
        for (int i = 0; i < rawPathComponent.length; i++) {
            if (rawPathComponent[i].contains(":")) {
                String componentName = rawPathComponent[i].substring(1);
                route.setPathParameters(componentName, new PathParameterImpl(componentName, i, pathComponent[i]));
            }
        }
    }

    private Map<String, RouteComponent> getRoutes() {
        return routeRegistry.getRoutes();
    }
}

package org.karunamay.core.api.router;

import org.karunamay.core.api.controller.RestControllerConfig;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public interface RouteRegistry {

    Map<String, RouteComponent> getRoutes();

    Map<String, String> getRoutesNameMapper();

    void add(String parentRouteName, Supplier<List<RouteComponent>> listSupplier);

    void add(Supplier<List<RouteComponent>> routes);

    void configureRoutes();
}

package org.karunamay.core.api.router;

import org.karunamay.core.api.controller.RestControllerConfig;
import org.karunamay.core.api.http.HttpRequest;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public interface RouteRegistry {

    Map<String, RouteComponent<?>> getRoutes();

    Map<String, String> getRoutesNameMapper();

    <T extends RestControllerConfig> RouteComponent<T> getRoute(String key);

    void add(String parentRouteName, Supplier<List<RouteComponent<?>>> listSupplier);

    void add(Supplier<List<RouteComponent<?>>> routes);

//    ThreadLocal<HttpRequest> getHttpRequest();
}

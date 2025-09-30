package org.karunamay.core.api.middleware;

import org.karunamay.core.api.controller.RestControllerConfig;
import org.karunamay.core.api.http.ApplicationContext;
import org.karunamay.core.api.router.RouteResolver;

public interface Middleware {
    void handle(ApplicationContext context, Runnable next);

    default RouteResolver getRouteResolver(ApplicationContext context) {
        return new RouteResolver(context);
    }
}

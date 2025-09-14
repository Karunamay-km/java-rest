package org.karunamay.core.internal;

import org.karunamay.core.api.config.ConfigManager;
import org.karunamay.core.api.controller.RestControllerConfig;
import org.karunamay.core.api.http.HttpContext;
import org.karunamay.core.api.http.HttpRequest;
import org.karunamay.core.api.router.RouteRegistry;
import org.karunamay.core.api.router.RouterConfig;
import org.karunamay.core.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ServiceLoader;

public class HttpRequestDispatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestDispatcher.class);

    public static void dispatch(HttpContext httpContext) {

        HttpRequest request = httpContext.getRequest();
        RouteRegistry registry = RouteRegistryImpl.getInstance();

        ServiceLoader<RouterConfig> loader = ServiceLoader.load(RouterConfig.class);
        for (RouterConfig config : loader) {
            config.configure(registry);
        }
        Route<?> route = RouteRegistryImpl.getInstance().getRoute(request.getPath());
        Class<?> cls = route.getController();

//        System.out.println(ConfigManager.getDatabasePath());
        System.out.println(ConfigManager.getInstance().getDatabasePath());

        try {
            if (RestControllerConfig.class.isAssignableFrom(cls)) {
                RestControllerConfig dispatcherClass = (RestControllerConfig) cls.getDeclaredConstructor().newInstance();
                switch (request.getMethod()) {
                    case POST -> dispatcherClass.post(httpContext);
                    case PUT -> dispatcherClass.put(httpContext);
                    case PATCH -> dispatcherClass.patch(httpContext);
                    case DELETE -> dispatcherClass.delete(httpContext);
                    default -> dispatcherClass.get(httpContext);
                }
            } else {
                throw new IllegalStateException(
                        String.format("class %s must implements the RestControllerConfig interface.", cls.getName())
                );
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

}

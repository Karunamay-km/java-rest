package org.karunamay.core.internal;

import org.karunamay.core.Error.HttpError;
import org.karunamay.core.api.controller.RestControllerConfig;
import org.karunamay.core.api.http.ApplicationContext;
import org.karunamay.core.api.http.HttpRequest;
import org.karunamay.core.api.router.RouteComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequestDispatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestDispatcher.class);

    public static void dispatch(ApplicationContext applicationContext) {
        try {
            HttpRequest request = applicationContext.getRequest();
            RouteRegistryImpl.setHttpRequest(applicationContext.getRequest());
            RouteComponent<RestControllerConfig> route = RouteRegistryImpl.getInstance().getRoute(request.getPath());
            if (route == null) HttpError.notFound404(applicationContext);
            else {
                applicationContext.setPathParameter(route.getPathParameters());
                Class<?> cls = route.getController();
                if (RestControllerConfig.class.isAssignableFrom(cls)) {
                    RestControllerConfig dispatcherClass =
                            (RestControllerConfig) cls.getDeclaredConstructor().newInstance();
                    switch (request.getMethod()) {
                        case POST -> dispatcherClass.post(applicationContext);
                        case PUT -> dispatcherClass.put(applicationContext);
                        case PATCH -> dispatcherClass.patch(applicationContext);
                        case DELETE -> dispatcherClass.delete(applicationContext);
                        default -> dispatcherClass.get(applicationContext);
                    }
                } else {
                    throw new IllegalStateException(
                            String.format("class %s must implements the RestControllerConfig interface.",
                                    cls.getName())
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
    }

}

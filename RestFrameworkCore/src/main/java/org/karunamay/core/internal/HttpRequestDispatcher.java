package org.karunamay.core.internal;

import org.karunamay.core.Error.HttpError;
import org.karunamay.core.api.controller.RestControllerConfig;
import org.karunamay.core.api.http.ApplicationContext;
import org.karunamay.core.api.http.HttpRequest;
import org.karunamay.core.api.router.RouteComponent;

import org.karunamay.core.api.router.RouteResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

public class HttpRequestDispatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestDispatcher.class);

    public static void dispatch(ApplicationContext applicationContext) {
        try {
            HttpRequest request = applicationContext.getHttpRequest();
            RouteComponent route = applicationContext.getRoute();
            if (route == null) HttpError.controllerNotFound404(applicationContext);
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
        } catch (NoSuchMethodException | InstantiationException
                 | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
    }

}

package org.karunamay.core.api.controller;


import org.karunamay.core.api.http.ApplicationContext;

public interface RestControllerConfig {

    void get(ApplicationContext context);
    void post(ApplicationContext httpContext);
    void put(ApplicationContext httpContext);
    void patch(ApplicationContext httpContext);
    void delete(ApplicationContext httpContext);
}

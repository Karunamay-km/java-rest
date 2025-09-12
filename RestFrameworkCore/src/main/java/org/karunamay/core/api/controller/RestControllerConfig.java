package org.karunamay.core.api.controller;


import org.karunamay.core.api.http.HttpContext;

public interface RestControllerConfig {

    void get(HttpContext context);
    void post(HttpContext httpContext);
    void put(HttpContext httpContext);
    void patch(HttpContext httpContext);
    void delete(HttpContext httpContext);
}

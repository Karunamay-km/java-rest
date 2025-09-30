package org.karunamay.core.api.controller;


import org.karunamay.core.api.http.ApplicationContext;

public interface RestControllerConfig {

    default void get(ApplicationContext context) {
    }

    default void post(ApplicationContext httpContext) {
    }

    default void put(ApplicationContext httpContext) {
    }

    default void patch(ApplicationContext httpContext) {
    }

    default void delete(ApplicationContext httpContext) {
    }
}

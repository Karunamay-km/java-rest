package org.karunamay.core.api.middleware;

import org.karunamay.core.api.http.ApplicationContext;

public interface Middleware {
    void handle(ApplicationContext context, Runnable next);
}

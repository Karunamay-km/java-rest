package org.karunamay.core.middleware;

import org.karunamay.core.api.http.ApplicationContext;
import org.karunamay.core.api.middleware.Middleware;

public class RequestMiddleware implements Middleware {
    @Override
    public void handle(ApplicationContext context, Runnable next) {

        // handle request middleware

//        next.run();

        System.out.println("request middleware");

    }
}

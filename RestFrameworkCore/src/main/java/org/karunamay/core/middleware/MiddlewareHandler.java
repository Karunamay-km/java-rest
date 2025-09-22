package org.karunamay.core.middleware;

import org.karunamay.core.api.http.ApplicationContext;
import org.karunamay.core.api.http.HttpResponseWriter;
import org.karunamay.core.api.http.HttpStatus;
import org.karunamay.core.api.middleware.Middleware;
import org.karunamay.core.exception.HttpResponseException;
import org.karunamay.core.http.HttpErrorResponse;
import org.karunamay.core.http.HttpResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;

public class MiddlewareHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MiddlewareHandler.class);

    public static boolean executePipeline(ApplicationContext context, List<Middleware> middlewares) {
        return execute(context, middlewares.listIterator());
    }

    private static boolean execute(ApplicationContext applicationContext, Iterator<Middleware> middlewareIterator) {
        if (middlewareIterator.hasNext()) {
            middlewareIterator.next().handle(applicationContext, () -> {
                execute(applicationContext, middlewareIterator);
            });
            return !middlewareIterator.hasNext();
        }
        return true;
    }

    public static void terminateExecution(ApplicationContext context) {
        HttpErrorResponse<Object> httpErrorResponse = new HttpErrorResponse<>(
                HttpStatus.HTTP_SERVER_ERROR.getCode(),
                "Middlewares are not connected. Did you forgot to call next.run()",
                null
        );
        try {
            HttpResponseWriter.sendServerError(context, httpErrorResponse);
        } catch (Exception e) {
            throw new HttpResponseException(e.getMessage());
        }
    }
}

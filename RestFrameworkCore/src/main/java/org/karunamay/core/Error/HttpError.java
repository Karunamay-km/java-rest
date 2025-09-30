package org.karunamay.core.Error;

import org.karunamay.core.api.http.ApplicationContext;
import org.karunamay.core.api.http.HttpResponseWriter;
import org.karunamay.core.api.http.HttpStatus;
import org.karunamay.core.http.HttpErrorResponse;

public class HttpError {
    public static void controllerNotFound404(ApplicationContext context) {
        HttpErrorResponse<Object> httpErrorResponse = new HttpErrorResponse<>(
                HttpStatus.HTTP_NOT_FOUND.getCode(),
                "Controller not found",
                null
        );
        HttpResponseWriter.send(httpErrorResponse, HttpStatus.HTTP_NOT_FOUND, context);
    }

    public static void unauthorizeAccess401(ApplicationContext context) {
        HttpErrorResponse<Object> httpErrorResponse = new HttpErrorResponse<>(
                HttpStatus.HTTP_UNAUTHORIZE_ACCESS.getCode(),
                "Unauthorize Access",
                null
        );
        HttpResponseWriter.send(httpErrorResponse, HttpStatus.HTTP_NOT_FOUND, context);

    }

    public static void badRequest400(ApplicationContext context) throws Exception {
        HttpResponseWriter.badSend(context);
    }

}

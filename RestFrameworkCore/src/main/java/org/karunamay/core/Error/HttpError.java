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

    public static void objectNotFound404(ApplicationContext context, String message) {
        HttpErrorResponse<Object> httpErrorResponse = new HttpErrorResponse<>(
                HttpStatus.HTTP_NOT_FOUND.getCode(),
                message,
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
        HttpResponseWriter.send(httpErrorResponse, HttpStatus.HTTP_UNAUTHORIZE_ACCESS, context);

    }

    public static void unauthorizeAccess401(ApplicationContext context, String message) {
        HttpErrorResponse<Object> httpErrorResponse = new HttpErrorResponse<>(
                HttpStatus.HTTP_UNAUTHORIZE_ACCESS.getCode(),
                message,
                null
        );
        HttpResponseWriter.send(httpErrorResponse, HttpStatus.HTTP_UNAUTHORIZE_ACCESS, context);

    }

    public static <T> void unauthorizeAccess401(ApplicationContext context, String message, T details) {
        HttpErrorResponse<Object> httpErrorResponse = new HttpErrorResponse<>(
                HttpStatus.HTTP_UNAUTHORIZE_ACCESS.getCode(),
                message,
                details
        );
        HttpResponseWriter.send(httpErrorResponse, HttpStatus.HTTP_UNAUTHORIZE_ACCESS, context);

    }

    public static void badRequest400(ApplicationContext context) {
        HttpErrorResponse<Object> httpErrorResponse = new HttpErrorResponse<>(
                HttpStatus.HTTP_BAD_REQUEST.getCode(),
                "Bad Request",
                null
        );
        HttpResponseWriter.send(httpErrorResponse, HttpStatus.HTTP_BAD_REQUEST, context);
    }

    public static void badRequest400(ApplicationContext context, String message) {
        HttpErrorResponse<Object> httpErrorResponse = new HttpErrorResponse<>(
                HttpStatus.HTTP_BAD_REQUEST.getCode(),
                message,
                null
        );
        HttpResponseWriter.send(httpErrorResponse, HttpStatus.HTTP_BAD_REQUEST, context);
    }
}

package org.karunamay.core.api.http;

import org.karunamay.core.exception.HttpResponseException;
import org.karunamay.core.http.AbstractHttpResponseWriter;

import java.io.IOException;

public class HttpResponseWriter extends AbstractHttpResponseWriter {
    public static <T> void send(T body, HttpStatus status, ApplicationContext context) {
        try {
            responseSend(body, status, context);
        } catch (IOException e) {
            throw new HttpResponseException(e.getMessage());
        }
    }
}

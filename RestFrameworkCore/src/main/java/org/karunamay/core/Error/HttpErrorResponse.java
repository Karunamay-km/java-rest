package org.karunamay.core.Error;

import org.karunamay.core.api.http.ApplicationContext;

public class HttpErrorResponse {

    private ApplicationContext httpContext;

    public HttpErrorResponse(ApplicationContext httpContext) {
        this.httpContext = httpContext;
    }

    public static void NOT_FOUND() {



//        HttpResponseWriter.send();
    }

}

package org.karunamay.core.middleware;

import org.karunamay.core.api.http.ApplicationContext;
import org.karunamay.core.api.http.HttpHeader;
import org.karunamay.core.api.http.HttpResponseWriter;
import org.karunamay.core.api.http.HttpStatus;
import org.karunamay.core.api.middleware.Middleware;
import org.karunamay.core.authentication.JWT.Jwt;
import org.karunamay.core.http.HttpErrorResponse;

public class AuthenticationMiddleware implements Middleware {
    @Override
    public void handle(ApplicationContext context, Runnable next) {

        HttpHeader headers = context.getRequest().getHeaders();
        Jwt jwt = new Jwt();

        if (!headers.asMap().containsKey("Authorization")) {
            unauthorizeAccess(context);
        } else {
            String accessToken = headers.asMap().get("Authorization").split(" ")[1];
            boolean isTokenValid = jwt.verifyToken(accessToken);
            if (!isTokenValid) {
                unauthorizeAccess(context);
            }
            next.run();
        }

    }

    private void unauthorizeAccess(ApplicationContext context) {
        HttpResponseWriter.send(
                new HttpErrorResponse<>(
                        HttpStatus.HTTP_UNAUTHORIZE_ACCESS.getCode(),
                        "Unauthorize Access",
                        null
                ),
                HttpStatus.HTTP_UNAUTHORIZE_ACCESS,
                context
        );
    }
}

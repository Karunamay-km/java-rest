package org.karunamay.core.middleware;

import org.karunamay.core.Error.HttpError;
import org.karunamay.core.api.http.ApplicationContext;
import org.karunamay.core.api.http.HttpHeader;
import org.karunamay.core.api.middleware.Middleware;
import org.karunamay.core.api.router.RouteComponent;
import org.karunamay.core.authentication.JWT.Jwt;
import org.karunamay.core.api.router.RouteResolver;

public class AuthenticationMiddleware implements Middleware {
    @Override
    public void handle(ApplicationContext context, Runnable next) {

        RouteResolver routeResolver = new RouteResolver(context);
        HttpHeader headers = context.getHttpRequest().getHeaders();
        RouteComponent routeComponent =
                routeResolver.resolve(context.getHttpRequest().getPath());

        Jwt jwt = new Jwt();
        if (!headers.asMap().containsKey("Authorization")
                && !routeComponent.isPublicRoute()) {
            HttpError.unauthorizeAccess401(context);
        } else {
            if (!routeComponent.isPublicRoute()) {
                String accessToken = headers.asMap().get("Authorization").split(" ")[1];
                boolean isTokenValid = jwt.verifyToken(accessToken);
                if (!isTokenValid) {
                    HttpError.unauthorizeAccess401(context);
                }
            }
            next.run();
        }

    }
}

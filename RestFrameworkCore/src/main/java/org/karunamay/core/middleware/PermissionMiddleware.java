package org.karunamay.core.middleware;

import org.karunamay.core.Error.HttpError;
import org.karunamay.core.api.annotation.Permission;
import org.karunamay.core.api.controller.RestControllerConfig;
import org.karunamay.core.api.http.*;
import org.karunamay.core.api.middleware.Middleware;
import org.karunamay.core.api.model.Role;
import org.karunamay.core.api.router.RouteComponent;
import org.karunamay.core.authentication.JWT.Jwt;
import org.karunamay.core.http.HttpErrorResponse;
import org.karunamay.core.api.router.RouteResolver;
import org.karunamay.core.model.UserModel;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PermissionMiddleware implements Middleware {
    @Override
    public void handle(ApplicationContext context, Runnable next) {

        HttpHeader headers = context.getHttpRequest().getHeaders();
        HttpRequest request = context.getHttpRequest();
        RouteResolver routeResolver = new RouteResolver(context);
        Jwt jwt = new Jwt();

        RouteComponent routeComponent =
                routeResolver.resolve(request.getPath());

        if (routeComponent == null) {
            HttpError.controllerNotFound404(context);
        } else {

            Class<?> clazz = routeComponent.getController();
            String methodName = request.getMethod().toString().toLowerCase();

            try {
                Method method = clazz.getMethod(methodName, ApplicationContext.class);
                if (!context.getRoute().isPublicRoute()) {
                    String accessToken = headers.asMap().get("Authorization").split(" ")[1];
                    UserModel user = jwt.parseClaims(accessToken).getPayload().get("user", UserModel.class);

                    Map<Integer, Role> roles = Arrays.stream(method.getAnnotation(Permission.class).role())
                            .collect(Collectors.toMap(
                                    Role::getPrivilege,
                                    Function.identity()
                            ));
                    if (!roles.containsKey(user.getRole().getPrivilege())) {
                        HttpError.unauthorizeAccess401(context);
                    }
                } else {
                    next.run();
                }
            } catch (NoSuchMethodException e) {
                HttpResponseWriter.send(
                        new HttpErrorResponse<>(HttpStatus.HTTP_SERVER_ERROR.getCode(),
                                "Method " + methodName +
                                        " in " + clazz.getSimpleName() +
                                        " not found. May be incorrect implementation of " +
                                        RestControllerConfig.class.getSimpleName(),
                                null
                        ),
                        HttpStatus.HTTP_NOT_FOUND,
                        context
                );
            }
        }
    }
}
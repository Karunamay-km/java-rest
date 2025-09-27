package org.karunamay.core.middleware;

import org.karunamay.core.api.annotation.Permission;
import org.karunamay.core.api.controller.RestControllerConfig;
import org.karunamay.core.api.http.*;
import org.karunamay.core.api.middleware.Middleware;
import org.karunamay.core.api.model.Role;
import org.karunamay.core.api.router.RouteComponent;
import org.karunamay.core.authentication.JWT.Jwt;
import org.karunamay.core.http.HttpErrorResponse;
import org.karunamay.core.internal.RouteRegistryImpl;
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
        HttpRequest request = context.getRequest();
        Jwt jwt = new Jwt();

        RouteComponent<RestControllerConfig> routeComponent =
                RouteRegistryImpl.getInstance().getRoute(request.getPath());

        if (routeComponent == null) {
            HttpResponseWriter.send(
                    new HttpErrorResponse<>(HttpStatus.HTTP_NOT_FOUND.getCode(),
                            "Controller Not Found", context),
                    HttpStatus.HTTP_NOT_FOUND,
                    null
            );
        } else {

            Class<?> clazz = routeComponent.getController();
            String requiredMethodName = request.getMethod().toString().toLowerCase();

            try {
                Method method = clazz.getDeclaredMethod(requiredMethodName);
                String accessToken = headers.asMap().get("Authorization").split(" ")[1];
                UserModel user = jwt.parseClaims(accessToken).getPayload().get("user", UserModel.class);

                Map<Integer, Role> roles = Arrays.stream(method.getAnnotation(Permission.class).role())
                        .collect(Collectors.toMap(
                                Role::getPrivilege,
                                Function.identity()
                        ));
                if (!roles.containsKey(user.getRole().getPrivilege())) {
                    HttpResponseWriter.send(
                            new HttpErrorResponse<>(
                                    HttpStatus.HTTP_UNAUTHORIZE_ACCESS.getCode(), "Permission denied",
                                    context
                            ),
                            HttpStatus.HTTP_UNAUTHORIZE_ACCESS,
                            null
                    );
                } else {
                    next.run();
                }
            } catch (NoSuchMethodException e) {
                HttpResponseWriter.send(
                        new HttpErrorResponse<>(HttpStatus.HTTP_SERVER_ERROR.getCode(),
                                "Method " + requiredMethodName +
                                        " in " + clazz.getSimpleName() +
                                        " not found. May be incorrect implementation of " +
                                        RestControllerConfig.class.getSimpleName(),
                                context
                        ),
                        HttpStatus.HTTP_NOT_FOUND,
                        null
                );
            }
        }
    }
}
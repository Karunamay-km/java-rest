package org.karunamay.core.controller;

import org.karunamay.core.api.authentication.AuthenticationService;
import org.karunamay.core.api.controller.RestControllerConfig;
import org.karunamay.core.api.dto.UserLoginResponseDTO;
import org.karunamay.core.api.http.ApplicationContext;
import org.karunamay.core.exception.NoPostBodyException;
import org.karunamay.core.http.HttpSuccessResponse;
import org.karunamay.core.http.RestHttpResponse;

public class LoginController implements RestControllerConfig {

    AuthenticationService authenticationService = new AuthenticationService();

    @Override
    public void post(ApplicationContext context) {
        System.out.println("body: " + context.getHttpRequest().getBody());
        if (context.getHttpRequest().getBody().isEmpty() ) {
            throw new NoPostBodyException("Post body is empty");
        }


    }
}

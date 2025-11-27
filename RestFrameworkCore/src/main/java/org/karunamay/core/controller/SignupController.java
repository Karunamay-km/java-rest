package org.karunamay.core.controller;

import org.karunamay.core.Error.HttpError;
import org.karunamay.core.api.authentication.AuthenticationService;
import org.karunamay.core.api.controller.RestControllerConfig;
import org.karunamay.core.api.dto.UserResponseDTO;
import org.karunamay.core.api.dto.UserSignupDTO;
import org.karunamay.core.api.http.ApplicationContext;
import org.karunamay.core.api.http.HttpResponseWriter;
import org.karunamay.core.api.http.HttpStatus;
import org.karunamay.core.exception.NoPostBodyException;
import org.karunamay.core.exception.ObjectNotFoundException;
import org.karunamay.core.http.HttpErrorResponse;

public class SignupController implements RestControllerConfig {

    AuthenticationService authenticationService = new AuthenticationService();

    @Override
    public void post(ApplicationContext context) {
        try {
            if (context.getHttpRequest().getBody().isEmpty()) {
                throw new NoPostBodyException("User details are missing. Please provide username, email and password");
            } else {
                UserSignupDTO body = context.getHttpRequest().parseBody(UserSignupDTO.class);
                UserResponseDTO userLoginResponseDTO = authenticationService.signup(body);
                HttpResponseWriter.send(
                        new HttpErrorResponse<>(
                                HttpStatus.HTTP_CREATE.getCode(),
                                "User created successfully",
                                userLoginResponseDTO
                        ),
                        HttpStatus.HTTP_CREATE,
                        context
                );
            }
        } catch (NoPostBodyException e) {
            HttpError.badRequest400(context, e.getMessage());
        } catch (ObjectNotFoundException e) {
            HttpError.objectNotFound404(context, e.getMessage());
        }
    }
}

package org.karunamay.core.api.authentication;

import org.karunamay.core.api.dto.UserDTO;
import org.karunamay.core.api.dto.UserLoginResponseDTO;
import org.karunamay.core.api.dto.UserResponseDTO;
import org.karunamay.core.api.service.UserService;
import org.karunamay.core.authentication.JWT.Jwt;
import org.karunamay.core.repository.UserRepository;
import org.karunamay.core.model.UserModel;
import org.karunamay.core.exception.InvalidCredentialsException;
import org.karunamay.core.http.RestHttpResponse;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.Map;

public class AuthenticationService {

    private final UserService userService = new UserService();
    private final UserRepository userRepository = new UserRepository();
    private final Jwt jwt = new Jwt();
    private final String TOKEN_TYPE = "Bearer";

    public UserResponseDTO signup(UserDTO dto) {
        return this.userService.createUser(dto);
    }

    public UserLoginResponseDTO login(String username, String password) {
        UserModel user = userService.getUserEntityByField("username", username);
        if (!user.isPasswordMatched(password)) {
            throw new InvalidCredentialsException("Password didn't matched");

        }

        jwt.setClaims(Map.of("user", user));
        String accessToken = jwt.createJwt();
        String refreshToken = jwt.createRefreshToken();
        return new UserLoginResponseDTO(
                accessToken,
                refreshToken,
                TOKEN_TYPE,
                jwt.getAccessTokenExpiration()
                        .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL)));
    }

    public RestHttpResponse<Object> changePassword(
            Long userId, String oldPassword, String newPassword) {
        UserModel user = userService.getUserEntityById(userId);
        if (user.isPasswordMatched(oldPassword)) {
            Map<String, Object> dto = new HashMap<>(
                    Map.of("password", newPassword)
            );
            userService.partialUserUpdate(userId, dto);
        }
        throw new InvalidCredentialsException("Password didn't matched");
    }


//    private Optional<?> createTokenResponse

}

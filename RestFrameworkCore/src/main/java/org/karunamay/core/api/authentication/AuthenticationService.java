package org.karunamay.core.api.authentication;

import org.karunamay.core.api.dto.*;
import org.karunamay.core.api.service.RoleService;
import org.karunamay.core.api.service.UserService;
import org.karunamay.core.authentication.JWT.Jwt;
import org.karunamay.core.exception.PasswordMismatchException;
import org.karunamay.core.model.RoleModel;
import org.karunamay.core.model.UserModel;
import org.karunamay.core.exception.InvalidCredentialsException;
import org.karunamay.core.security.PasswordHasher;

import java.time.Instant;
import java.util.Map;

public class AuthenticationService {

    private final UserService userService = new UserService();
    private final RoleService roleService = new RoleService();
    private final Jwt jwt = new Jwt();

    public UserResponseDTO signup(UserSignupDTO dto) {
        if (!dto.password1().equals(dto.password2())) {
            throw new PasswordMismatchException("Both password didn't matched");
        }

        RoleModel roleModel = roleService.getRoleEntityByField("role", dto.role().toLowerCase());
        UserDTO userDTO = UserDTO.builder()
                .username(dto.username())
                .email(dto.email())
                .password(PasswordHasher.hash(dto.password1()))
                .role(roleModel)
                .build();
        return this.userService.createUser(userDTO);
    }

    public UserLoginResponseDTO login(LoginRequestDTO credentials) {
        UserModel user = userService.getUserEntityByField("email", credentials.email());
        if (!PasswordHasher.verify(user.getPassword(), credentials.password())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        String accessToken = jwt.createJwt();
        String refreshToken = jwt.createRefreshToken();
        jwt.setSubject(user.getUsername());
        jwt.setClaims(Map.of("user", user));

        userService.partialUserUpdate(user.getID(), Map.of("lastLogin", Instant.now()));

        return UserLoginResponseDTO
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwt.getAccessTokenExpiration().toString())
                .build();
    }

}

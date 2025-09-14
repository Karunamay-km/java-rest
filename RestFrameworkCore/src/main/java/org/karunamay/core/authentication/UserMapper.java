package org.karunamay.core.authentication;

import org.karunamay.core.api.authentication.UserDTO;
import org.karunamay.core.api.authentication.UserResponseDTO;
import org.karunamay.core.authentication.model.UserModel;

public class UserMapper {
    public static UserResponseDTO toResponseDTO(UserModel user) {
        return new UserResponseDTO(
                user.getID(), user.getUsername(), user.getPassword(), user.getEmail(), user.getLastLogin(),
                user.getCreatedAt(), user.getUpdatedAt()
        );
    }

    public static UserDTO toDTO(UserModel user) {
        return new UserDTO(user.getUsername(), user.getPassword(), user.getEmail());
    }
}

package org.karunamay.core.authentication;

import org.karunamay.core.api.authentication.UserDTO;
import org.karunamay.core.api.authentication.UserResponseDTO;
import org.karunamay.core.authentication.model.UserModel;

public class UserMapper {
    public static UserResponseDTO toResponseDTO(UserModel user) {
        return new UserResponseDTO.Builder()
                .withId(user.getID())
                .withEmail(user.getEmail())
                .withUsername(user.getUsername())
                .withLastLogin(user.getLastLogin())
                .withCreatedAt(user.getCreatedAt())
                .withUpdatedAt(user.getUpdatedAt())
                .build();
    }

    public static UserDTO toDTO(UserModel user) {
        return new UserDTO(user.getUsername(), user.getPassword(), user.getEmail());
    }
}

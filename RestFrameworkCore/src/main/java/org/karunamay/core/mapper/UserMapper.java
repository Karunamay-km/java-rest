package org.karunamay.core.mapper;

import org.karunamay.core.api.dto.UserDTO;
import org.karunamay.core.api.dto.UserResponseDTO;
import org.karunamay.core.model.UserModel;

public class UserMapper {
    public static UserResponseDTO toResponseDTO(UserModel user) {
        return UserResponseDTO.builder()
                .ID(user.getID())
                .email(user.getEmail())
                .username(user.getUsername())
                .lastLogin(user.getLastLogin())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public static UserDTO toDTO(UserModel user) {
        return new UserDTO(user.getUsername(), user.getPassword(), user.getEmail(), user.getRole());
    }
}

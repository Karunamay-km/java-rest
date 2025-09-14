package org.karunamay.core.api.authentication;

public record UserDTO(String username,
               String password,
               String email) {
}

package org.karunamay.core.api.dto;

import lombok.Builder;

@Builder
public record UserDTO(String username, String password, String email) {
}

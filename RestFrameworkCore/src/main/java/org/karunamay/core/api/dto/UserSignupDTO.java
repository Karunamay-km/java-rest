package org.karunamay.core.api.dto;

import lombok.Builder;

@Builder
public record UserSignupDTO(
        String username,
        String email,
        String password1,
        String password2,
        String role
) {
}

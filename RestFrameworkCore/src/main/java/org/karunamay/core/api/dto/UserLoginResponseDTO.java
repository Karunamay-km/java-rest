package org.karunamay.core.api.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserLoginResponseDTO(
        String accessToken,
        String refreshToken,
        String tokenType,
        String expiresIn
) {
}

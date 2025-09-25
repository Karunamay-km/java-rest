package org.karunamay.core.api.dto;

import lombok.Builder;

import java.time.Instant;
import java.time.LocalDateTime;

@Builder
public record UserResponseDTO(Long ID,
                              String username,
                              String email,
                              Instant lastLogin,
                              LocalDateTime createdAt,
                              LocalDateTime updatedAt) {
}



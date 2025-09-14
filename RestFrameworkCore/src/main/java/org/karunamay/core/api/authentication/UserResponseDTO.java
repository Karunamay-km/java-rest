package org.karunamay.core.api.authentication;

import java.time.Instant;
import java.time.LocalDateTime;

public record UserResponseDTO(Long ID,
                              String username,
                              String password,
                              String email,
                              Instant lastLogin,
                              LocalDateTime createdAt,
                              LocalDateTime updatedAt) {
}

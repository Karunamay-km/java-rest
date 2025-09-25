package org.karunamay.core.api.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RoleResponseDTO(
        Long id,
        String role,
        boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

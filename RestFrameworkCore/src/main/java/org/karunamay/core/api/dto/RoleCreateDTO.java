package org.karunamay.core.api.dto;

import lombok.Builder;

@Builder
public record RoleCreateDTO (String role, boolean isActive) {
}

package org.karunamay.core.authentication;

import org.karunamay.core.api.dto.RoleResponseDTO;
import org.karunamay.core.authentication.model.UserRoleModel;

public class RoleMapper {

    public static RoleResponseDTO toRoleResponseDTO(UserRoleModel role) {
        return RoleResponseDTO.builder()
                .id(role.getId())
                .role(role.getRole())
                .isActive(role.getIsActive())
                .createdAt(role.getCreatedAt())
                .updatedAt(role.getUpdatedAt())
                .build();
    }
}

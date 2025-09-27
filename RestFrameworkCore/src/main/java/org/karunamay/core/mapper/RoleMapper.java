package org.karunamay.core.mapper;

import org.karunamay.core.api.dto.RoleResponseDTO;
import org.karunamay.core.model.RoleModel;

public class RoleMapper {

    public static RoleResponseDTO toResponseDTO(RoleModel role) {
        return RoleResponseDTO.builder()
                .id(role.getId())
                .role(role.getRole())
                .isActive(role.getIsActive())
                .createdAt(role.getCreatedAt())
                .updatedAt(role.getUpdatedAt())
                .build();
    }
}

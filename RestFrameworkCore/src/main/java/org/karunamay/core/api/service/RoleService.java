package org.karunamay.core.api.service;

import org.karunamay.core.api.dto.RoleCreateDTO;
import org.karunamay.core.api.dto.RoleResponseDTO;
import org.karunamay.core.authentication.RoleMapper;
import org.karunamay.core.authentication.RoleRepository;

import java.util.Optional;

public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Optional<RoleResponseDTO> getRoleById (Long id) {
        return roleRepository.getById(id).map(RoleMapper::toRoleResponseDTO);
    }

    public Optional<RoleResponseDTO> create(RoleCreateDTO dto) {
        return this.roleRepository.create(dto).map(RoleMapper::toRoleResponseDTO);
    }

    public Optional<RoleResponseDTO> update(Long id, RoleCreateDTO dto) {
        return this.roleRepository.update(id, dto).map(RoleMapper::toRoleResponseDTO);
    }

}

package org.karunamay.core.api.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;

import org.karunamay.core.api.dto.RoleCreateDTO;
import org.karunamay.core.api.dto.RoleResponseDTO;
import org.karunamay.core.exception.DatabaseOperationException;
import org.karunamay.core.exception.DuplicateObjectException;
import org.karunamay.core.exception.ObjectNotFoundException;
import org.karunamay.core.exception.ServiceException;
import org.karunamay.core.mapper.RoleMapper;
import org.karunamay.core.model.RoleModel;
import org.karunamay.core.repository.RoleRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RoleService {

    private static final Logger logger = LoggerFactory.getLogger(RoleService.class);
    private final RoleRepository roleRepository = new RoleRepository();
    private final Map<String, RoleModel> roleCache =
            this.roleRepository.getAll().orElseGet(Collections::emptyList)
                    .stream()
                    .collect(Collectors.toMap(
                            RoleModel::getRole,
                            Function.identity()
                    ));

    public RoleModel getRoleEntityById(Long id) {
        return this.roleRepository
                .getById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Role with id" + id + " not found"));
    }

    public RoleResponseDTO getRoleDTOById(Long id) {
        return this.roleRepository
                .getById(id)
                .map(RoleMapper::toResponseDTO)
                .orElseThrow(() -> new ObjectNotFoundException("Role with id" + id + " not found"));
    }

    public RoleModel getRoleEntityByField(String fieldName, String value) {
        return this.roleRepository
                .getByField(fieldName, value)
                .orElseThrow(() -> new ObjectNotFoundException("Role does not exist with " + fieldName + " " + value));
    }

    public RoleResponseDTO getRoleDTOByField(String fieldName, String value) {
        return this.roleRepository
                .getByField(fieldName, value)
                .map(RoleMapper::toResponseDTO)
                .orElseThrow(() -> new ObjectNotFoundException("Role does not exist with " + fieldName + " " + value));
    }

    public Optional<RoleResponseDTO> getRoleById(Long id) {
        return this.roleRepository.getById(id).map(RoleMapper::toResponseDTO);
    }

    public RoleResponseDTO create(RoleCreateDTO dto) {
        try {
            return roleRepository
                    .create(dto)
                    .map(RoleMapper::toResponseDTO)
                    .orElseThrow(() -> new IllegalStateException("Failed to persist role"));
        } catch (EntityExistsException e) {
            throw new DuplicateObjectException("Role with name " + dto.role() + " already exists", e);
        } catch (PersistenceException e) {
            throw new DatabaseOperationException("Database error occurred while creating role", e);
        } catch (Exception e) {
            logger.error("Failed to create role with name {}", dto.role());
            throw new ServiceException("Unexpected error occurred while creating role", e);
        }
    }

    public RoleResponseDTO update(Long id, RoleCreateDTO dto) {
        try {
            return roleRepository
                    .update(id, dto)
                    .map(RoleMapper::toResponseDTO)
                    .orElseThrow(() -> new IllegalStateException("Failed to merge role"));
        } catch (EntityNotFoundException e) {
            throw new ObjectNotFoundException("Role with id" + id + " not found");
        } catch (PersistenceException e) {
            throw new DatabaseOperationException("Database error occurred while creating user", e);
        } catch (Exception e) {
            throw new ServiceException("Unexpected error occurred while updating role", e);
        }
    }

    public RoleResponseDTO partialUserUpdate(Long id, Map<String, Object> fieldStringMap) {
        try {
            return roleRepository.partialUpdate(id, fieldStringMap)
                    .map(RoleMapper::toResponseDTO)
                    .orElseThrow(() -> new IllegalStateException("Failed to merge role"));
        } catch (PersistenceException e) {
            throw new DatabaseOperationException("Database error occurred while updating role", e);
        } catch (Exception e) {
            throw new ServiceException("Unexpected error occurred while creating role", e);
        }
    }

    public Map<String, RoleModel> getRoleCache() {
        return roleCache;
    }
}

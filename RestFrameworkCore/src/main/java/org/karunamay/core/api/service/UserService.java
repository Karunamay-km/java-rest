package org.karunamay.core.api.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import org.karunamay.core.api.Model.Role;
import org.karunamay.core.api.dto.UserDTO;
import org.karunamay.core.api.dto.UserResponseDTO;
import org.karunamay.core.mapper.UserMapper;
import org.karunamay.core.model.RoleModel;
import org.karunamay.core.repository.UserRepository;
import org.karunamay.core.model.UserModel;
import org.karunamay.core.exception.DatabaseOperationException;
import org.karunamay.core.exception.DuplicateObjectException;
import org.karunamay.core.exception.ObjectNotFoundException;
import org.karunamay.core.exception.ServiceException;
import org.karunamay.core.security.PasswordHasher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository = new UserRepository();
    private final RoleService roleService = new RoleService();

    public UserResponseDTO createUser(UserDTO user) {
        try {
            String hashPassword = PasswordHasher.hash(user.getPassword());
            user.setPassword(hashPassword);
            return userRepository
                    .createUser(user)
                    .map(UserMapper::toResponseDTO)
                    .orElseThrow(() -> new IllegalStateException("Failed to persist user"));
        } catch (EntityExistsException e) {
            throw new DuplicateObjectException("User with email " + user.getEmail() + " already exists", e);
        } catch (PersistenceException e) {
            throw new DatabaseOperationException("Database error occurred while creating user", e);
        } catch (Exception e) {
            throw new ServiceException("Unexpected error occurred while creating user", e);
        }

    }

    public void createAdmin() {
        if (!this.userRepository.isObjectExistsByField("admin")) {
            RoleModel role = this.roleService.getRoleEntityByField("role", Role.ADMIN.getName());
            UserDTO admin = new UserDTO(
                    "admin",
                    "admin@email.com",
                    "admin",
                    role);
            this.createUser(admin);
        }
    }

    public UserResponseDTO updateUser(Long id, UserDTO user) {
        try {
            return userRepository
                    .updateUser(id, user)
                    .map(UserMapper::toResponseDTO)
                    .orElseThrow(() -> new IllegalStateException("Failed to merge user"));
        } catch (EntityNotFoundException e) {
            throw new ObjectNotFoundException("User with id" + id + " not found");
        } catch (PersistenceException e) {
            throw new DatabaseOperationException("Database error occurred while creating user", e);
        } catch (Exception e) {
            throw new ServiceException("Unexpected error occurred while creating user", e);
        }
    }

    public UserResponseDTO partialUserUpdate(Long id, Map<String, Object> fieldStringMap) {
        try {
            return userRepository.partialUpdateUser(id, fieldStringMap)
                    .map(UserMapper::toResponseDTO)
                    .orElseThrow(() -> new IllegalStateException("Failed to merge user"));
        } catch (PersistenceException e) {
            throw new DatabaseOperationException("Database error occurred while creating user", e);
        } catch (Exception e) {
            throw new ServiceException("Unexpected error occurred while creating user", e);
        }
    }

    public UserModel getUserEntityById(Long id) {
        return this.userRepository
                .getById(id)
                .orElseThrow(() -> new ObjectNotFoundException("User with id" + id + " not found"));
    }

    public UserResponseDTO getUserDTOById(Long id) {
        return this.userRepository
                .getById(id)
                .map(UserMapper::toResponseDTO)
                .orElseThrow(() -> new ObjectNotFoundException("User with id" + id + " not found"));
    }

    public UserModel getUserEntityByField(String fieldName, String value) {
        return this.userRepository
                .getByField(fieldName, value)
                .orElseThrow(() -> new ObjectNotFoundException("User does not exist with " + fieldName + " " + value));
    }

    public UserResponseDTO getUserDTOByField(String fieldName, String value) {
        return this.userRepository
                .getByField(fieldName, value)
                .map(UserMapper::toResponseDTO)
                .orElseThrow(() -> new ObjectNotFoundException("User does not exist with " + fieldName + " " + value));
    }

}

package org.karunamay.core.api.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import org.karunamay.core.api.dto.UserDTO;
import org.karunamay.core.api.dto.UserResponseDTO;
import org.karunamay.core.authentication.UserMapper;
import org.karunamay.core.authentication.UserRepository;
import org.karunamay.core.authentication.model.UserModel;
import org.karunamay.core.exception.DatabaseOperationException;
import org.karunamay.core.exception.DuplicateObjectException;
import org.karunamay.core.exception.ObjectNotFoundException;
import org.karunamay.core.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository = new UserRepository();

    public boolean isUserExist(Long id) {
        UserResponseDTO user = this.getUserDTOById(id);
        return user != null;
    }

    public boolean isUserExist(String fieldName, String value) {
        UserResponseDTO user = this.getUserDTOByField(fieldName, value);
        return user != null;
    }

    public UserResponseDTO createUser(UserDTO user) {
        try {
            return userRepository
                    .create(user)
                    .map(UserMapper::toResponseDTO)
                    .orElseThrow(() -> new IllegalStateException("Failed to persist user"));
        } catch (EntityExistsException e) {
            throw new DuplicateObjectException("User with email " + user.email() + " already exists", e);
        } catch (PersistenceException e) {
            throw new DatabaseOperationException("Database error occurred while creating user", e);
        } catch (Exception e) {
            throw new ServiceException("Unexpected error occurred while creating user", e);
        }

    }

    public Optional<UserResponseDTO> createAdmin() {
        return this.userRepository.createAdmin().map(UserMapper::toResponseDTO);
    }

    public UserResponseDTO updateUser(Long id, UserDTO user) {
        try {
            return userRepository
                    .update(id, user)
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

    public UserResponseDTO partialUserUpdate(Long id, Map<String, String> fieldStringMap) {
        try {
            return userRepository.partialUpdate(id, fieldStringMap)
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

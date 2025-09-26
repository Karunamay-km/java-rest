package org.karunamay.core.authentication;

import org.karunamay.core.api.dto.UserDTO;
import org.karunamay.core.authentication.model.UserModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

public class UserRepository extends BaseRepository<UserModel> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRepository.class);

    public UserRepository() {
        super(UserModel.class);
    }

    public Optional<UserModel> createUser(UserDTO user) {
        try {
            return this.create(user);
        } catch (Exception e) {
            LOGGER.error("Failed to createUser user with email {} and password {}", user.getUsername(), user.getPassword());
            throw e;
        }
    }

    public Optional<UserModel> updateUser(Long id, UserDTO userDTO) {
        try {
            return this.update(id, userDTO);
        } catch (Exception e) {
            LOGGER.error("Failed to update user with id {}", id);
            throw e;
        }
    }

    public Optional<UserModel> partialUpdateUser(Long id, Map<String, Object> fieldStringMap) {
        try {
            return this.partialUpdate(id, fieldStringMap);
        } catch (Exception e) {
            LOGGER.error("Failed to update user with id " + id);
            throw new RuntimeException(e);
        }
    }
}

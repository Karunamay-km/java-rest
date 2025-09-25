package org.karunamay.core.authentication;

import org.karunamay.core.api.dto.UserDTO;
import org.karunamay.core.authentication.model.ModelRepository;
import org.karunamay.core.authentication.model.UserModel;
import org.karunamay.core.db.DatabaseManager;
import org.karunamay.core.security.PasswordHasher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

public class UserRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRepository.class);

    private ModelRepository<UserModel> model = new ModelRepository<>(UserModel.class);

    private final String DEFAULT_ADMIN = "admin";
    private final String DEFAULT_PASSWORD = "admin";

    public Optional<UserModel> getById(Long id) {
        return model.findById(id);
    }

    public Optional<UserModel> getByField(String fieldName, String value) {
        return model.findByField(fieldName, value);
    }


    public Optional<UserModel> create(UserDTO user) {
        try {
            return DatabaseManager.executeWrite(em -> {
                String hashedPassword = PasswordHasher.hash(user.password());
                UserModel userModel =
                        new UserModel(
                                user.username(),
                                user.email(),
                                hashedPassword
                        );
                em.persist(userModel);
                return Optional.of(userModel);
            });
        } catch (Exception e) {
            LOGGER.error("Failed to create user with email {} and password {}", user.username(), user.password());
            throw e;
        }
    }

    public Optional<UserModel> update(Long id, UserDTO userDTO) {
        return DatabaseManager.executeWrite(em -> {
            try {
                Optional<UserModel> userModel = model.findById(id);

                if (userModel.isEmpty()) return Optional.empty();

                UserModel user = userModel.get();
                user.updateUsername(userDTO.username());
                user.updateEmail(userDTO.email());

                em.merge(user);

                return Optional.of(user);
            } catch (Exception e) {
                LOGGER.error("Failed to update user with id {}", id);
                throw e;
            }
        });
    }

    public Optional<UserModel> partialUpdate(Long id, Map<String, String> fieldStringMap) {
        return DatabaseManager.executeWrite(em -> {
            try {
                Optional<UserModel> userOpt = model.findById(id);
                if (userOpt.isEmpty()) return Optional.empty();

                UserModel user = userOpt.get();

                for (Map.Entry<String, String> entry : fieldStringMap.entrySet()) {
                    Field field = UserModel.class.getDeclaredField(entry.getKey());
                    field.setAccessible(true);
                    field.set(user, entry.getValue());
                }

                em.merge(user);

                return Optional.of(user);
            } catch (Exception e) {
                LOGGER.error("Failed to update user with id " + id);
                throw new RuntimeException(e);
            }
        });
    }

    public Optional<UserModel> createAdmin() {
        Optional<UserModel> userModel = model.findByField("username", DEFAULT_ADMIN);
        if (userModel.isEmpty()) {
            return DatabaseManager.executeWrite(em -> {
                String password = PasswordHasher.hash(DEFAULT_PASSWORD);
                UserModel user = new UserModel(DEFAULT_ADMIN, "", password);
                em.persist(user);
                return Optional.of(user);
            });
        }

        return userModel;
    }
}

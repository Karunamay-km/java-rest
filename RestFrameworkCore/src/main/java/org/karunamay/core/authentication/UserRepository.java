package org.karunamay.core.authentication;

import jakarta.persistence.NoResultException;
import org.karunamay.core.api.authentication.UserDTO;
import org.karunamay.core.api.authentication.UserResponseDTO;
import org.karunamay.core.authentication.model.UserModel;
import org.karunamay.core.db.DatabaseManager;
import org.karunamay.core.security.PasswordHasher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

class UserRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRepository.class);

    private final String DEFAULT_ADMIN = "admin";
    private final String DEFAULT_PASSWORD = "admin";

    private Optional<UserModel> findModelById(Long id) {
        return DatabaseManager.executeRead(em -> Optional.ofNullable(em.find(UserModel.class, id)));
    }

    public Optional<UserResponseDTO> findById(Long id) {
        return Optional.of(UserMapper.toResponseDTO(this.findModelById(id).get()));
    }

    public Optional<UserResponseDTO> findByUsername(String username) {
        return DatabaseManager.executeRead(em -> {
            try {
                return Optional.of(
                        UserMapper.toResponseDTO(em
                                .createQuery("SELECT u FROM UserModel u WHERE u.username = :username", UserModel.class)
                                .setParameter("username", username)
                                .getSingleResult()
                        )
                );
            } catch (NoResultException e) {
                return Optional.empty();
            }
        });
    }

    public Optional<UserResponseDTO> create(UserDTO user) {
        return DatabaseManager.executeWrite(em -> {
            UserModel userModel =
                    new UserModel(
                            user.username(),
                            user.email(),
                            user.password()
                    );
            em.persist(userModel);
            return Optional.of(UserMapper.toResponseDTO(userModel));
        });
    }

    public Optional<UserResponseDTO> update(Long id, UserDTO userDTO) {
        return DatabaseManager.executeWrite(em -> {
            try {
                UserModel user = this.findModelById(id).get();
                user.setUsername(userDTO.username());
                user.setEmail(userDTO.email());
                return Optional.of(UserMapper.toResponseDTO(user));
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
                return Optional.empty();
            }
        });
    }


    public Optional<UserResponseDTO> createAdmin() {
        Optional<UserResponseDTO> userResponseDTO = this.findByUsername(DEFAULT_ADMIN);
        if (userResponseDTO.isEmpty()) {
            return DatabaseManager.executeWrite(em -> {
                String password = PasswordHasher.hash(DEFAULT_PASSWORD);
                UserModel userModel = new UserModel(DEFAULT_ADMIN, "", password);
                em.persist(userModel);
                return Optional.of(UserMapper.toResponseDTO(userModel));
            });
        }
        return userResponseDTO;
    }
}

package org.karunamay.core.authentication;

import org.karunamay.core.api.authentication.UserDTO;
import org.karunamay.core.api.authentication.UserResponseDTO;

import java.util.Optional;

public class UserService {

    private final UserRepository userRepository = new UserRepository();

    public Optional<UserResponseDTO> createUser (UserDTO user) {
        return userRepository.create(user);
    }

    public Optional<UserResponseDTO> createAdmin () {
        return this.userRepository.createAdmin();
    }

    public Optional<UserResponseDTO> createAdditionalAdmin () {
        return this.userRepository.createAdmin();
    }

    public Optional<UserResponseDTO> updateUser(Long id, UserDTO user) {
        return this.userRepository.update(id, user);
    }

    public Optional<UserResponseDTO> getUserById(Long id) {
        return this.userRepository.findById(id);
    }



}

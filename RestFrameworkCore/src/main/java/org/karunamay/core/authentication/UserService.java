package org.karunamay.core.authentication;

import org.karunamay.core.api.authentication.model.UserModel;

public class UserService {

    private final UserRepository userRepository = new UserRepository();

    public UserModel createUser (String username, String email, String password) {
        UserModel user = new UserModel(username, email, password);
        return userRepository.save(user);
    }

    public UserModel createDefaultUser () {
        return this.createUser("admin", "", "admin");
    }
}

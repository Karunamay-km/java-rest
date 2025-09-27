package org.karunamay.core.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "User")
public class UserModel extends AbstractBaseUserModel {

    public UserModel() {
        super();
    }

    public UserModel(String username, String email, String password) {
        super(username, password, email);
    }

    public void updateUsername(String username) {
        this.setUsername(username);
    }

    public void updateEmail(String email) {
        this.setUsername(email);
    }

    public void updatePassword(String password) {
        this.changePassword(password);
    }

    public boolean isPasswordMatched(String password) {
        return this.matchPassword(password);
    }

}

package org.karunamay.core.authentication.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.lang.reflect.Field;
import java.util.List;

@Entity
@Table(name = "UserTable")
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

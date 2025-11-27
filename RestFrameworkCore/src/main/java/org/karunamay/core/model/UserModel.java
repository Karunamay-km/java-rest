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
}

package org.karunamay.core.api.authentication.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.karunamay.core.authentication.model.AbstractBaseUserModel;

@Entity
@Table(name = "UserTable")
public class UserModel extends AbstractBaseUserModel {

    public UserModel() {
        super();
    }

    public UserModel(String username, String email, String password) {
        super(username, password, email);
    }
}

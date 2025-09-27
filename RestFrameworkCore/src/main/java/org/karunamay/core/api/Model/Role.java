package org.karunamay.core.api.Model;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN(0, "admin"),
    STAFF(1, "staff");

    final int privilege;
    final String name;

    Role(int privilege, String name) {
        this.privilege = privilege;
        this.name = name;
    }
}

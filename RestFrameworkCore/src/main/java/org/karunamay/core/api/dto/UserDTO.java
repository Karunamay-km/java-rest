package org.karunamay.core.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import org.karunamay.core.model.RoleModel;

@Builder
@Getter
@Setter
public class UserDTO {
    private String username;
    private String password;
    private String email;
    private RoleModel role;

    public UserDTO(String username, String password, String email, RoleModel role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }
}
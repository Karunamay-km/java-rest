package org.karunamay.core.authentication;

import org.karunamay.core.authentication.model.UserRoleModel;

public class RoleRepository extends BaseRepository<UserRoleModel> {
    public RoleRepository() {
        super(UserRoleModel.class);
    }
}

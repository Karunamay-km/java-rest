package org.karunamay.core.repository;

import org.karunamay.core.model.RoleModel;

public class RoleRepository extends BaseRepository<RoleModel> {
    public RoleRepository() {
        super(RoleModel.class);
    }
}

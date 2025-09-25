package org.karunamay.core.authentication;

import org.karunamay.core.api.dto.RoleCreateDTO;
import org.karunamay.core.authentication.model.ModelRepository;
import org.karunamay.core.authentication.model.UserRoleModel;
import org.karunamay.core.db.DatabaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class RoleRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleRepository.class);

    private ModelRepository<UserRoleModel> model = new ModelRepository<>(UserRoleModel.class);

    private Optional<UserRoleModel> findModelById(Long id) {
        return DatabaseManager.executeRead(em -> Optional.ofNullable(em.find(UserRoleModel.class, id)));
    }

    public Optional<UserRoleModel> getById(Long id) {
        return model.findById(id);
    }

    public Optional<UserRoleModel> create(RoleCreateDTO dto) {
        try {
            return DatabaseManager.executeWrite(em -> {
                UserRoleModel role = new UserRoleModel(
                        dto.role().toLowerCase(),
                        dto.isActive()
                );
                em.persist(role);
                return Optional.of(role);
            });
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return Optional.empty();
        }

    }

    public Optional<UserRoleModel> update(Long id, RoleCreateDTO dto) {
        try {

            Optional<UserRoleModel> roleModel = this.getById(id);

            return DatabaseManager.executeWrite(em -> {

                if (roleModel.isEmpty()) return Optional.empty();

                UserRoleModel role = roleModel.get();
                role.setRole(dto.role());
                role.setIsActive(dto.isActive());

                em.merge(role);

                return Optional.of(role);
            });

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return Optional.empty();
        }
    }

}

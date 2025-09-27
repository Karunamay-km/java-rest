package org.karunamay.core.api;

import org.karunamay.core.api.model.Role;
import org.karunamay.core.api.config.ConfigManager;
import org.karunamay.core.api.dto.RoleCreateDTO;
import org.karunamay.core.api.service.RoleService;
import org.karunamay.core.authentication.JWT.Jwt;
import org.karunamay.core.api.service.UserService;
import org.karunamay.core.db.DatabaseManager;
import org.karunamay.core.exception.ApplicationContextException;
import org.karunamay.core.internal.RouteRegistryImpl;
import org.karunamay.core.internal.WebServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ApplicationRunner {

    private final int PORT = 8080;
    private final int THREADS = 10;

    private ConfigManager cfg;
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationRunner.class);
    private final UserService userService = new UserService();
    private final RoleService roleService = new RoleService();
    private final WebServer server = new WebServer(PORT, THREADS);

    public ApplicationRunner(ConfigManager configManager) {
        this.cfg = configManager;
    }

    public void run() {
        try {

            initializeDefaultRoles();
            RouteRegistryImpl.configureRoutes();
            this.userService.createAdmin();

            System.out.println("Routes: " + RouteRegistryImpl.getInstance().getRoutes());

            Jwt.generateKeys();
            LOGGER.info("Default superuser created with username 'admin' and password 'admin'");
            server.start();

        } catch (Exception e) {
            LOGGER.error("Application startup failed", e);
            terminate();
            throw new ApplicationContextException(e);
        }
    }

    public void terminate() {
        try {
            server.stop();
            LOGGER.info("Server stopped.");
        } catch (Exception e) {
            LOGGER.warn("Failed to stop server ", e);
        }
        try {
            DatabaseManager.shutdownEntityManager();
            LOGGER.info("Database connection closed.");
        } catch (Exception e) {
            LOGGER.warn("Failed to close database connection ", e);
        }
    }

    private void initializeDefaultRoles() {

        List<RoleCreateDTO> dtoList = new ArrayList<>();
        for (Role role : Role.values()) {
            dtoList.add(
                    new RoleCreateDTO(role.getName(), true, role.getPrivilege())
            );
        }

        for (RoleCreateDTO dto : dtoList) {
            if (!this.roleService.getRoleCache().containsKey(dto.role())) {
                this.roleService.create(dto);
            }
        }
    }
}

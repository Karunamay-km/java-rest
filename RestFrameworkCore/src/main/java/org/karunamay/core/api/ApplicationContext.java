package org.karunamay.core.api;

import org.karunamay.core.api.authentication.UserDTO;
import org.karunamay.core.api.authentication.UserResponseDTO;
import org.karunamay.core.authentication.model.UserModel;
import org.karunamay.core.api.config.ConfigManager;
import org.karunamay.core.authentication.UserService;
import org.karunamay.core.db.DatabaseManager;
import org.karunamay.core.exception.ApplicationContextException;
import org.karunamay.core.internal.WebServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationContext {

    private final int PORT = 8080;
    private final int THREADS = 10;

    private ConfigManager cfg;
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationContext.class);
    private final UserService userService = new UserService();

    public ApplicationContext(ConfigManager configManager) {
        this.cfg = configManager;
    }

    public void run() {

        WebServer server = new WebServer(PORT, THREADS);

        try {

            DatabaseManager.init();
            UserResponseDTO admin =  this.userService.createAdmin().get();
//            if (!cfg.getAuthenticatedUsers().isEmpty()) {
//                for(UserDTO user : cfg.getAuthenticatedUsers()) {
//                    this.userService.createAdmin()
//                }
//            }


            LOGGER.info("User created with username {} and password {}", admin.username(), admin.password());

            server.start();

        } catch (Exception e) {
            server.stop();
            DatabaseManager.shutdownEntityManager();
            LOGGER.error(e.getMessage());
            throw new ApplicationContextException(e);
        }
    }
}

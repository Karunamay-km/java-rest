package org.karunamay.core.api;

import org.karunamay.core.api.authentication.model.UserModel;
import org.karunamay.core.api.config.ConfigManager;
import org.karunamay.core.authentication.UserService;
import org.karunamay.core.db.DatabaseManager;
import org.karunamay.core.exception.ApplicationContextException;
import org.karunamay.core.internal.WebServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationContext {

    private ConfigManager cfg;
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationContext.class);
    private final UserService userService = new UserService();

    public ApplicationContext(ConfigManager configManager) {
        this.cfg = configManager;
    }

    public void run() {

        WebServer server = new WebServer(8080, 10);

        try {

            DatabaseManager.init();
            UserModel user = this.userService.createDefaultUser();

            LOGGER.info("Database created on {}", this.cfg.getDatabasePath());
            LOGGER.info("User created with username {} and password {}", user.getUsername(), user.getPassword());

            server.start();

        } catch (Exception e) {
//            server.stop();
            LOGGER.error(e.getMessage());
            throw new ApplicationContextException(e);
        }
    }
}

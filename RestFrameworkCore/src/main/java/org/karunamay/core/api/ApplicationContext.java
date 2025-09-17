package org.karunamay.core.api;

import org.karunamay.core.api.config.ConfigManager;
import org.karunamay.core.authentication.JWT.Jwt;
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
    private final WebServer server = new WebServer(PORT, THREADS);

    public ApplicationContext(ConfigManager configManager) {
        this.cfg = configManager;
    }

    public void run() {
        try {

            this.userService.createAdmin().get();
            Jwt.generateKeys();
            Jwt jwt = new Jwt();
            System.out.println(jwt.createJwt());

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
}

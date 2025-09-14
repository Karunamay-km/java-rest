package org.karunamay.core.api.config;

import org.karunamay.core.api.authentication.UserDTO;
import org.karunamay.core.authentication.model.UserModel;

import java.util.List;

public class ConfigManager {

    private static final ConfigManager instance = new ConfigManager();
    private String databasePath;
    private List<UserDTO> authenticatedUsers;
    private String cwd = System.getProperty("user.dir");

    public String getCwd() {
        return cwd;
    }

    public void setCwd(String cwd) {
        this.cwd = cwd;
    }

    public String getDatabasePath() {
        return this.databasePath;
    }

    private void setDatabasePath(String url) {
        this.databasePath = url;
    }

    private void setAuthenticatedUsers(List<UserDTO> authenticatedUsers) {
        this.authenticatedUsers = authenticatedUsers;
    }

    public List<UserDTO> getAuthenticatedUsers() {
        return authenticatedUsers;
    }

    public static ConfigManager getInstance() {
        return instance;
    }

    public static Builder configure() {
        return new Builder(getInstance());
    }

    public static class Builder {

        private final ConfigManager cfg;

        private Builder(ConfigManager cfg) {
            this.cfg = cfg;
        }

        public Builder withDatabasePath(String url) {
            this.cfg.setDatabasePath(url);
            return this;
        }

        public Builder withAuthenticatedUser(List<UserDTO> users) {
            this.cfg.setAuthenticatedUsers(users);
            return this;
        }

        public ConfigManager build() {
            return cfg;
        }
    }
}

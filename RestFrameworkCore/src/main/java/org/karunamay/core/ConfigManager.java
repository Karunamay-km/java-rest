package org.karunamay.core;

public class ConfigManager {

    private static final ConfigManager INSTANCE = new ConfigManager();
    private final String cwd;
    private final String packageName;

    private ConfigManager() {
        this.cwd = System.getProperty("user.dir");
        this.packageName = ConfigManager.class.getPackageName();
    }

    public static ConfigManager getInstance() {
        return INSTANCE;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getCwd() {
        return cwd;
    }

    public String getRoutePath() {
        return packageName + ".routes";
    }
}

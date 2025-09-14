package org.karunamay.core.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class DatabaseManager {

    public static EntityManagerFactory manager;

    public static void init() {
        try {
            manager = Persistence.createEntityManagerFactory("myPU");
        } catch (Exception e) {
            e.printStackTrace();
            Throwable cause = e.getCause();
            while (cause != null) {
                cause.printStackTrace();
                cause = cause.getCause();
            }
            throw new RuntimeException(e);
        }
    }

    public static EntityManager getEntityManager() {
        return manager.createEntityManager();
    }

    public static void shutdownEntityManager() {
        if (manager.isOpen()) {
            manager.close();
        }
    }
}

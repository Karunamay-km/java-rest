package org.karunamay.core.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.Optional;
import java.util.function.Function;

public class DatabaseManager {

    public static EntityManagerFactory manager = init();

    public static EntityManagerFactory init() {
        try {
            return Persistence.createEntityManagerFactory("myPU");
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

    public static EntityManagerFactory getEntityManagerfactory () {
        return manager;
    }

    public static EntityManager getEntityManager() {
        return getEntityManagerfactory().createEntityManager();
    }

    public static void shutdownEntityManager() {
        if (getEntityManagerfactory().isOpen()) {
            getEntityManagerfactory().close();
        }
    }

    public static <T> Optional<T> executeWrite(Function<EntityManager, Optional<T>> action) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Optional<T> result = action.apply(em);
            tx.commit();
            return result;
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            em.close();
        }
    }

    public static <T> Optional<T> executeRead(Function<EntityManager , Optional<T>> action) {
        try (EntityManager em = getEntityManager()) {
            return action.apply(em);
        }
    }
}

package org.karunamay.core.authentication;

import jakarta.persistence.EntityManager;
import org.karunamay.core.api.authentication.model.UserModel;
import org.karunamay.core.db.DatabaseManager;

import java.util.Optional;

class UserRepository {
    public UserModel save (UserModel user) {
        try (EntityManager em = DatabaseManager.getEntityManager()) {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
            return user;
        } finally {
            DatabaseManager.shutdownEntityManager();
        }
    }

    public Optional<UserModel> findById (Long id) {
        try (EntityManager em = DatabaseManager.getEntityManager()) {
            return Optional.ofNullable(em.find(UserModel.class, id));
        } finally {
            DatabaseManager.shutdownEntityManager();
        }
    }

}

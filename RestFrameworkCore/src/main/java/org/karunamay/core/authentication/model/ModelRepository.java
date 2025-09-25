package org.karunamay.core.authentication.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.karunamay.core.db.DatabaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.function.Function;

public class ModelRepository<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelRepository.class);

    private final Class<T> modelClass;

    public ModelRepository(Class<T> modelClass) {
        this.modelClass = modelClass;
    }

    public Optional<T> findById(Long id) {
        try {
            Optional<T> obj = DatabaseManager
                    .executeRead(
                            em -> Optional.ofNullable(em.find(modelClass, id))
                    );
            if (obj.isEmpty()) {
                throw new EntityNotFoundException("entity of " + modelClass.getSimpleName() + " with id " + id + " not found");
            }
            return obj;
        } catch (Exception e) {
            LOGGER.error("Error while fetching model {} with id {} : {}",
                    modelClass.getSimpleName(), id, e.getMessage(), e);
            throw e;
        }
    }

    public Optional<T> findByField(String fieldName, String value) {
        try {

            StringBuilder query = new StringBuilder();

            query.append("SELECT u FROM ");
            query.append(modelClass.getName());
            query.append(String.format(" u WHERE u.%s = u:%s", fieldName, fieldName));

            Optional<T> obj = DatabaseManager.executeRead(
                    em -> Optional.of(
                            em.createQuery(query.toString(), modelClass)
                                    .setParameter(fieldName, value)
                                    .getSingleResult()
                    )
            );
            if (obj.isEmpty()) {
                throw new EntityNotFoundException(
                        "entity of " + modelClass.getSimpleName() + " with field " + fieldName + " not found");
            }
            return obj;
        } catch (Exception e) {
            LOGGER.error("Error while fetching model {} with id {} : {}",
                    modelClass.getSimpleName(), value, e.getMessage(), e);
            throw e;
        }
    }

}

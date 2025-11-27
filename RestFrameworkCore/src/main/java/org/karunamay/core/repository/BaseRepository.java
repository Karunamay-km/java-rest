package org.karunamay.core.repository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Table;
import jakarta.persistence.TypedQuery;
import org.karunamay.core.db.DatabaseManager;
import org.karunamay.core.utils.ReflectiveDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

abstract class BaseRepository<T> {

    private static final Logger logger = LoggerFactory.getLogger(BaseRepository.class);

    protected Class<T> modelClass;
    protected Map<String, Field> fieldCache;

    public BaseRepository(Class<T> modelClass) {
        this.modelClass = modelClass;
        this.fieldCache = ReflectiveDTO.getAllFields(modelClass);
    }

    public boolean isObjectExistsById(Long id) {
        return this.getById(id).isPresent();
    }

    public boolean isObjectExistsByField(String username) {
        return this.getByField("username", username).isPresent();
    }

    public Optional<List<T>> getAll() {
        try {
            return DatabaseManager
                    .executeRead(
                            em -> Optional.ofNullable(em.createQuery(
                                    "SELECT r FROM " + modelClass.getSimpleName() + " r",
                                    modelClass
                            ).getResultList())
                    );
        } catch (Exception e) {
            logger.error("Error while fetching model collection");
            throw e;
        }
    }

    public Optional<T> getById(Long id) {
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
            logger.error("Error while fetching model {} with id {} : {}",
                    modelClass.getSimpleName(), id, e.getMessage(), e);
            throw e;
        }
    }

    public Optional<T> getByField(String fieldName, String value) {
        try {

            StringBuilder queryString = new StringBuilder();

            queryString.append("SELECT u FROM ");
            queryString.append(modelClass.getSimpleName());
            queryString.append(String.format(" u WHERE u.%s = :%s", fieldName, fieldName));

            Optional<T> obj = DatabaseManager.executeRead(
                    em -> {
                        List<T> resultList = em.createQuery(queryString.toString(), modelClass)
                                .setParameter(fieldName, value)
                                .getResultList();
                        return resultList.isEmpty() ? Optional.empty() : Optional.of(resultList.get(0));
                    }
            );
            return obj;
        } catch (Exception e) {
            logger.error("Error while fetching model {} with id {} : {}",
                    modelClass.getSimpleName(), value, e.getMessage(), e);
            throw e;
        }
    }

    public <D> Optional<T> create(D dto) {
        return DatabaseManager.executeWrite(em -> {
            try {
                Constructor<T> constructor = this.modelClass.getDeclaredConstructor();
                constructor.setAccessible(true);

                T model = constructor.newInstance();

                for (Field field : dto.getClass().getDeclaredFields()) {
                    field.setAccessible(true);

                    if (!this.fieldCache.containsKey(field.getName())) {
                        throw new RuntimeException(
                                field.getName() + " field doesn't exists on " + this.modelClass.getSimpleName());
                    }

                    Field modelClassField = this.fieldCache.get(field.getName());
                    modelClassField.setAccessible(true);

                    Object value = field.get(dto);

                    if (value != null && !modelClassField.getType().isAssignableFrom(field.getType())) {
                        throw new IllegalArgumentException("Type mismatch for field " + field.getName() +
                                ": expected " + modelClassField.getType() +
                                ", got " + field.getType()
                        );
                    }
                    modelClassField.set(model, value);
                }

                em.persist(model);

                return Optional.of(model);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                     InvocationTargetException | SecurityException e) {
                throw new RuntimeException(
                        "Failed to createUser model " + this.modelClass.getSimpleName() + " instance", e);
            }
        });
    }

    public <D> Optional<List<T>> bulkCreate(List<D> dtoList) {
        List<T> collection = new ArrayList<>();
        for (D dto : dtoList) {
            Optional<T> obj = this.create(dto);
            collection.add(obj.get());
        }
        return Optional.of(collection);
    }

    public <D> Optional<T> update(Long id, D dto) {

        return DatabaseManager.executeWrite(em -> {
            try {
                Optional<T> modelOpt = this.getById(id);
                if (modelOpt.isEmpty()) return Optional.empty();

                T model = modelOpt.get();

                for (Field field : dto.getClass().getDeclaredFields()) {

                    if (!this.fieldCache.containsKey(field.getName())) {
                        throw new RuntimeException(
                                field.getName() + " field doesn't exists on " + this.modelClass.getSimpleName());
                    }

                    Field modelClassField = this.fieldCache.get(field.getName());
                    modelClassField.setAccessible(true);

                    Object value = field.get(dto);

                    if (value != null && !modelClassField.getType().isAssignableFrom(field.getType())) {
                        throw new IllegalArgumentException("Type mismatch for field " + field.getName() +
                                ": expected " + modelClassField.getType() +
                                ", got " + field.getType()
                        );
                    }
                    modelClassField.set(model, value);
                }

                em.persist(model);

                return Optional.of(model);
            } catch (IllegalAccessException | SecurityException e) {
                throw new RuntimeException(
                        "Failed to update model " + this.modelClass.getSimpleName() + " instance", e);
            }
        });
    }

    public <D> Optional<T> partialUpdate(Long id, Map<String, Object> updates) {
        return DatabaseManager.executeWrite(em -> {
            try {
                Optional<T> userOpt = this.getById(id);
                if (userOpt.isEmpty()) return Optional.empty();

                T user = userOpt.get();

                for (Map.Entry<String, Object> entry : updates.entrySet()) {
                    if (!this.fieldCache.containsKey(entry.getKey())) {
                        throw new RuntimeException(
                                entry.getKey() + " field doesn't exists on " + this.modelClass.getSimpleName());
                    }

                    Field field = this.fieldCache.get(entry.getKey());
                    field.setAccessible(true);

                    Object value = entry.getValue();

                    if (value != null && !field.getType().isAssignableFrom(entry.getValue().getClass())) {
                        throw new IllegalArgumentException("Type mismatch for field " + entry.getKey() +
                                ": expected " + field.getType() +
                                ", got " + entry.getValue().getClass()
                        );
                    }

                    field.set(user, entry.getValue());
                }

                em.merge(user);

                return Optional.of(user);

            } catch (IllegalAccessException e) {
                throw new RuntimeException(
                        "Failed to update model " + this.modelClass.getSimpleName() + " instance", e);
            }
        });
    }
}

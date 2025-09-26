package org.karunamay.core.authentication;

import org.karunamay.core.authentication.model.ModelRepository;
import org.karunamay.core.db.DatabaseManager;
import org.karunamay.core.utils.ReflectiveDTO;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Optional;

public abstract class BaseRepository<T> {

    protected ModelRepository<T> modelRepository;
    protected Class<T> modelClass;
    protected Map<String, Field> fieldCache;

    public BaseRepository(Class<T> modelClass) {
        this.modelClass = modelClass;
        this.modelRepository = new ModelRepository<>(modelClass);
        this.fieldCache = ReflectiveDTO.getAllFields(modelClass);
    }

    public Optional<T> getById(Long id) {
        return modelRepository.findById(id);
    }

    public Optional<T> getByField(String fieldName, String value) {
        return modelRepository.findByField(fieldName, value);
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

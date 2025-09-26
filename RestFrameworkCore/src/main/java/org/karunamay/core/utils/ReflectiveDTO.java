package org.karunamay.core.utils;

import java.lang.reflect.Field;
import java.util.*;

public class ReflectiveDTO {

    public static Map<String, Object> createDto(Object object) {

        if (object == null) return null;

        Map<String, Object> data = new LinkedHashMap<>();
        Class<?> cls = object.getClass();

        for (Field field : cls.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Object value = field.get(object);
                data.put(field.getName(), value);

            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return data;
    }


    public static Map<String, Field> getAllFields(Class<?> clazz) {
        Map<String, Field> fields = new HashMap<>();
        Class<?> currentClazz = clazz;

        while (currentClazz != null && currentClazz != Object.class) {
            Field[] declearedField = currentClazz.getDeclaredFields();
            for (Field field : declearedField) {
                field.setAccessible(true);
                fields.put(field.getName(), field);
            }

            currentClazz = currentClazz.getSuperclass();
        }

        return fields;
    }

}

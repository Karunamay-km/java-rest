package org.karunamay.core.utils;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

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
}

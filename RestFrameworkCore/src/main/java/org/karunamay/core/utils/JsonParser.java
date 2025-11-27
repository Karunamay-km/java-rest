package org.karunamay.core.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonParser {

    private static ObjectMapper mapper = new ObjectMapper();

    {
        mapper.setDefaultPropertyInclusion(JsonInclude.Include.USE_DEFAULTS);
    }

    public static <T> String fromObjectToString(T object) throws JsonProcessingException {
        try {
            return mapper.writeValueAsString(ReflectiveDTO.createDto(object));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    public static <T> T fromStringToObject(String jsonString, TypeReference<T> typeReference) {
        try {
            return mapper.readValue(jsonString, typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromStringToObject(String jsonString, Class<T> clazz) {
        try {
            return mapper.readValue(jsonString, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}

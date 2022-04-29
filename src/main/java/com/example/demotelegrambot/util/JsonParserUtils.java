package com.example.demotelegrambot.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.io.UncheckedIOException;

public final class JsonParserUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.registerModule(new JavaTimeModule());
        MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    private JsonParserUtils() {
    }

    public static String getNodeValue(String key, String value) {
        JsonNode valueNode = convertToJsonNode(value).get(key);
        return valueNode == null ? null : valueNode.asText();
    }

    public static JsonNode convertToJsonNode(String value) {
        try {
            return MAPPER.readTree(value);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static String convertToJson(Object object) {
        try {
            return MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T convertToObject(String value, Class<T> valueClass) {
        try {
           return MAPPER.readValue(value, valueClass);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T convertToObject(String value, TypeReference<T> typeReference) {
        try {
            return MAPPER.readValue(value, typeReference);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T convert(Object value, Class<T> valueClass) {
        return MAPPER.convertValue(value, valueClass);
    }

}

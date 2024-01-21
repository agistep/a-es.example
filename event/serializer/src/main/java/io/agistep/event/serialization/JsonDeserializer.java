package io.agistep.event.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.agistep.event.Deserializer;

import java.util.Objects;

import static org.valid4j.Validation.validate;

public final class JsonDeserializer implements Deserializer {

    private final Class<?> aClass;

    public JsonDeserializer(Class<?> aClass) {
        validate(aClass != null, new IllegalArgumentException("null cannot deserialize"));
        validate(!aClass.isPrimitive(), new IllegalArgumentException("Primitive Type cannot deserialize"));

        this.aClass = aClass;
    }

    @Override
    public boolean isSupport(Object payload) {
        String p;
        if (payload instanceof String) {
            p = String.valueOf(payload);
        } else {
            return false;
        }
        return isJSONValid(p);
    }


    private static boolean isJSONValid(String jsonString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            return jsonNode.isObject();
        } catch (Exception e) {
            return false;
        }
    }


    @Override
    public Object deserialize(byte[] byteArray) {
        String s = new String(byteArray);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(s, aClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JsonDeserializer that)) return false;
        return Objects.equals(aClass, that.aClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(aClass);
    }

    @Override
    public Class<?> getTargetClazz() {
        return aClass;
    }
}

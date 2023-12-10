package io.agistep.event.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.agistep.event.Serializer;

import static org.valid4j.Validation.validate;

public class JsonSerializer implements Serializer {

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean isSupport(Object payload) {
        try {
            objectMapper = new ObjectMapper();
            String s = objectMapper.writeValueAsString(payload);
            return !s.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public byte[] serialize(Object payload) {
        validate(payload != null, new IllegalArgumentException("payload가 Null 이 될 수는 없습니다."));

        try {
            String s = objectMapper.writeValueAsString(payload);
            return s.getBytes();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && this.getClass() == obj.getClass();
    }

    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }
}

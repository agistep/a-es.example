package io.agistep.event.serialization;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.agistep.event.Serializer;

public class JsonSerializer implements Serializer {
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
    public byte[] serialize(Object payload) {
        return String.valueOf(payload).getBytes();
    }
}

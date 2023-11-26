package io.agistep.event.serializer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

class JsonObjectDeserializer implements Deserializer {
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
        return new String(byteArray);
    }
}

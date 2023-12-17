package io.agistep.event;

import io.agistep.event.serialization.NoOpDeserializer;
import io.agistep.event.serialization.NoOpSerializer;

import java.util.ArrayList;
import java.util.List;

class Cache {

    private final List<Serializer> serializers = new ArrayList<>();
    private final List<Deserializer> deSerializers = new ArrayList<>();

    public Serializer getSerializer(String className) {

        return serializers.stream()
                .filter(a -> a.getClass().getName().startsWith(className))
                .findFirst()
                .orElseGet(NoOpSerializer::new);
    }

    public void addSerializer(Serializer serializer) {
        serializers.add(serializer);
    }

    public Deserializer getDeserializer(String className) {
        return deSerializers.stream()
                .filter(a -> a.getClass().getName().startsWith(className))
                .findFirst()
                .orElseGet(NoOpDeserializer::new);
    }

    public void addDeserializer(Deserializer deserializer) {
        deSerializers.add(deserializer);
    }
}

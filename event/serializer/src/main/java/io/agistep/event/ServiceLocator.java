package io.agistep.event;

import io.agistep.event.serialization.NoOpDeserializer;
import io.agistep.event.serialization.NoOpSerializer;

class ServiceLocator {

    private static Cache cache = new Cache();

    static final String PREFIX = "io.agistep.event.serialization.";

    ServiceLocator(Cache cache) {
        ServiceLocator.cache = cache;
    }

    public static Serializer getSerializer(String name) {
        final String className = PREFIX + name + "Serializer";
        Serializer serializer = cache.getSerializer(className);

        if (!(serializer instanceof NoOpSerializer)) {
            return serializer;
        }

        InitialContext context = new InitialContext();

        Serializer serializerFromContext = context.serializerLookup(className);
        cache.addSerializer(serializerFromContext);
        return serializerFromContext;
    }

    public static Deserializer getDeserializer(String name) {
        final String className = PREFIX + name + "Deserializer";
        Deserializer deserializer = cache.getDeserializer(className);

        if (!(deserializer instanceof NoOpDeserializer)) {
            return deserializer;
        }

        InitialContext context = new InitialContext();

        Deserializer deserializeLookup = context.deserializerLookup(className);
        cache.addDeserializer(deserializeLookup);
        return deserializeLookup;
    }
}

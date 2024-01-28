package io.agistep.event;

import io.agistep.event.serialization.NoOpDeserializer;
import io.agistep.event.serialization.NoOpSerializer;

import static org.valid4j.Validation.validate;

public class SerializerProvider {

    private static SerializerCache serializerCache = new SerializerCache();
    private static final InitialContext context = new InitialContext();

    static final String PREFIX = "io.agistep.event.serialization.";

    SerializerProvider(SerializerCache serializerCache) {
        SerializerProvider.serializerCache = serializerCache;
    }

    public static Serializer getProtocolBufferSerializer() {
        return SerializerProvider.getSerializer("ProtocolBuffer");
    }

    public static Deserializer getProtocolBufferDeserializer(Class<?> targetClass) {
        return SerializerProvider.getDeserializer("ProtocolBuffer", targetClass);
    }

    public static Serializer getJsonSerializer() {
        return SerializerProvider.getSerializer("Json");
    }

    public static Deserializer getJsonDeSerializer(Class<?> targetClass) {
        return SerializerProvider.getDeserializer("Json", targetClass);
    }

    public static Serializer getSerializer(String name) {

        final String className = PREFIX + name + "Serializer";
        Serializer serializer = serializerCache.getSerializer(className);

        if (!(serializer instanceof NoOpSerializer)) {
            return serializer;
        }

        Serializer serializerFromContext = context.serializerLookup(className);

        if (serializerFromContext instanceof NoOpSerializer) {
            validate(name.endsWith("Serializer"), new UnsupportedOperationException("Class name must end with Serializer."));
            serializerFromContext = context.serializerLookup(name);
        }

        serializerCache.addSerializer(serializerFromContext);
        return serializerFromContext;
    }

    public static Deserializer getDeserializer(String name, Class<?> targetClass) {

        final String className = PREFIX + name + "Deserializer";
        Deserializer deserializer = serializerCache.getDeserializer(className, targetClass);

        if (!(deserializer instanceof NoOpDeserializer)) {
            return deserializer;
        }

        InitialContext context = new InitialContext();

        Deserializer deserializeLookup = context.deserializerLookup(className, targetClass);

        if (deserializeLookup instanceof NoOpDeserializer) {
            validate(name.endsWith("Deserializer"), new UnsupportedOperationException("Class name must end with Deserializer."));
            deserializeLookup = context.deserializerLookup(name, targetClass);
        }

        serializerCache.addDeserializer(deserializeLookup);
        return deserializeLookup;
    }
}

package io.agistep.event;

import io.agistep.event.serialization.NoOpDeserializer;
import io.agistep.event.serialization.NoOpSerializer;
import org.valid4j.Validation;

import static org.valid4j.Validation.*;
import static org.valid4j.Validation.validate;

public class SerializerProvider {

    private static Cache cache = new Cache();

    static final String PREFIX = "io.agistep.event.serialization.";

    SerializerProvider(Cache cache) {
        SerializerProvider.cache = cache;
    }

    public static Serializer getSerializer(String name) {

        final String className = PREFIX + name + "Serializer";
        Serializer serializer = cache.getSerializer(className);

        if (!(serializer instanceof NoOpSerializer)) {
            return serializer;
        }

        InitialContext context = new InitialContext();

        Serializer serializerFromContext = context.serializerLookup(className);

        if (serializerFromContext instanceof NoOpSerializer) {
            validate(name.endsWith("Serializer"), new UnsupportedOperationException("Class name must end with Serializer."));
            serializerFromContext = context.serializerLookup(name);
        }

        cache.addSerializer(serializerFromContext);
        return serializerFromContext;
    }

    public static Deserializer getDeserializer(String name, Class<?> targetClass) {

        final String className = PREFIX + name + "Deserializer";
        Deserializer deserializer = cache.getDeserializer(className);

        if (!(deserializer instanceof NoOpDeserializer)) {
            return deserializer;
        }

        InitialContext context = new InitialContext();

        Deserializer deserializeLookup = context.deserializerLookup(className, targetClass);

        if (deserializeLookup instanceof NoOpDeserializer) {
            validate(name.endsWith("Deserializer"), new UnsupportedOperationException("Class name must end with Deserializer."));
            deserializeLookup = context.deserializerLookup(name, targetClass);
        }

        cache.addDeserializer(deserializeLookup);
        return deserializeLookup;
    }
}

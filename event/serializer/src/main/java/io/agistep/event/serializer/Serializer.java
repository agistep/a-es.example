package io.agistep.event.serializer;

public interface Serializer {
    boolean isSupport(Object payload);

    byte[] serialize(Object payload);
}

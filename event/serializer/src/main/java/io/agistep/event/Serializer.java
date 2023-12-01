package io.agistep.event;

public interface Serializer {
    boolean isSupport(Object payload);

    byte[] serialize(Object payload);
}

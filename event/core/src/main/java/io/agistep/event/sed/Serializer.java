package io.agistep.event.sed;

public interface Serializer {
    boolean isSupport(Object payload);

    byte[] serialize(Object payload);
}

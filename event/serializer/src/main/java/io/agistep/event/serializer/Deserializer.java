package io.agistep.event.serializer;

public interface Deserializer {
    boolean isSupport(Object payload);
    Object deserialize(byte[] byteArray);
}

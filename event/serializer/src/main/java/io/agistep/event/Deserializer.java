package io.agistep.event;

public interface Deserializer {
    boolean isSupport(Object payload);
    Object deserialize(byte[] byteArray);
}

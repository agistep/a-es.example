package io.agistep.event.sed;

public interface Deserializer {
    boolean isSupport(Object payload);
    Object deserialize(byte[] byteArray);
}

package io.agistep.event.sed;

public interface Deserializer {
    Object deserialize(byte[] byteArray);
}

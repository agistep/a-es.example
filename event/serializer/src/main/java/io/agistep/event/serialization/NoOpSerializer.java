package io.agistep.event.serialization;

import io.agistep.event.Serializer;

public class NoOpSerializer implements Serializer {

    @Override
    public boolean isSupport(Object payload) {
        return false;
    }

    @Override
    public byte[] serialize(Object payload) {
        throw new UnsupportedOperationException();
    }
}

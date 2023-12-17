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

    @Override
    public boolean equals(Object obj) {
        return obj != null && this.getClass() == obj.getClass();
    }

    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }
}

package io.agistep.event.serialization;

import io.agistep.event.Deserializer;

public class NoOpDeserializer implements Deserializer {

    @Override
    public boolean isSupport(Object payload) {
        return false;
    }

    @Override
    public Object deserialize(byte[] byteArray) {
        throw new UnsupportedOperationException();
    }
}

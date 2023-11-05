package io.agistep.event.sed;

class JsonSerializer implements Serializer {
    @Override
    public boolean isSupport(Object payload) {
        return false;
    }

    @Override
    public byte[] serialize(Object payload) {
        return new byte[0];
    }
}

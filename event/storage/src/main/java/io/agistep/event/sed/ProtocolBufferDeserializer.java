package io.agistep.event.sed;

import com.google.protobuf.ByteString;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ProtocolBufferDeserializer implements Deserializer {
    private final Class<?> clazz;

    public ProtocolBufferDeserializer(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Object deserialize(byte[] byteArray) {
        try {
            Method parseFrom = clazz.getMethod("parseFrom", ByteString.class);
            return parseFrom.invoke(null, ByteString.copyFrom(byteArray));
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}

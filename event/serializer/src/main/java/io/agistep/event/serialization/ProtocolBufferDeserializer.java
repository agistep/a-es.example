package io.agistep.event.serialization;

import com.google.protobuf.ByteString;
import io.agistep.event.Deserializer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class ProtocolBufferDeserializer implements Deserializer {
    private final Class<?> clazz;

    public ProtocolBufferDeserializer(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public boolean isSupport(Object payload) {
        ByteString bytes = ByteString.copyFrom(String.valueOf(payload).getBytes(StandardCharsets.UTF_8));
        try {
            Object invoke = getObject(bytes);
            return invoke != null;
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            return false;
        }
    }

    @Override
    public Object deserialize(byte[] byteArray) {
        ByteString bytes = ByteString.copyFrom(byteArray);
        try {
            return getObject(bytes);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Class<?> getTargetClazz() {
        return clazz;
    }

    private Object getObject(ByteString bytes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method parseFrom = clazz.getMethod("parseFrom", ByteString.class);
        return parseFrom.invoke(null, bytes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProtocolBufferDeserializer that)) return false;
        return Objects.equals(clazz, that.clazz);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clazz);
    }
}

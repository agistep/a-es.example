package io.agistep.event.serialization;

import com.google.protobuf.Message;
import io.agistep.event.Serializer;

public class ProtocolBufferSerializer implements Serializer {
    @Override
    public boolean isSupport(Object payload) {
        return payload instanceof Message;
    }

    @Override
    public byte[] serialize(Object payload) {
        Message message = (Message) payload;
        return message.toByteArray();
    }
}

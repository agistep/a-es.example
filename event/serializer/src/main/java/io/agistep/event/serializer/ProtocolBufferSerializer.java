package io.agistep.event.serializer;

import com.google.protobuf.Message;

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

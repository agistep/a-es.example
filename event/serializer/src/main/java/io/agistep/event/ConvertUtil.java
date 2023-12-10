package io.agistep.event;

import io.agistep.event.serialization.JsonObjectDeserializer;
import io.agistep.event.serialization.JsonSerializer;
import io.agistep.event.serialization.ProtocolBufferDeserializer;
import io.agistep.event.serialization.ProtocolBufferSerializer;

import java.nio.charset.StandardCharsets;
import java.util.List;

public final class ConvertUtil {
    static final class PayloadSerialization {

        public static String convert(Object e) {
            List<Serializer> serializers = List.of(
                    new JsonSerializer(),
                    new ProtocolBufferSerializer());

            Serializer serializer = serializers.stream()
                    .filter(s -> s.isSupport(e))
                    .findFirst()
                    .orElseThrow(UnsupportedOperationException::new);
            return new String(serializer.serialize(e));
        }
    }

    static final class PayloadDeSerialization {

        public static Object convert(Object serialized, String name) {

            Class<?> clazz;
            try {
                clazz = Class.forName(name);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            List<Deserializer> deSerializers = List.of(
                    new JsonObjectDeserializer(clazz),
                    new ProtocolBufferDeserializer(clazz)
            );

            Deserializer deserializer = deSerializers.stream()
                    .filter(s -> s.isSupport(serialized))
                    .findFirst()
                    .orElseThrow(UnsupportedOperationException::new);

            return deserializer.deserialize(String.valueOf(serialized).getBytes(StandardCharsets.UTF_8));
        }
    }

    public static class EventDTO {

        long id;
        long seq;
        String name;
        long aggregateId;
        String payload;

        String occurredAt;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getSeq() {
            return seq;
        }

        public void setSeq(long seq) {
            this.seq = seq;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getAggregateId() {
            return aggregateId;
        }

        public void setAggregateId(long aggregateId) {
            this.aggregateId = aggregateId;
        }

        public String getPayload() {
            return payload;
        }

        public void setPayload(String payload) {
            this.payload = payload;
        }

        public String getOccurredAt() {
            return occurredAt;
        }

        public void setOccurredAt(String occurredAt) {
            this.occurredAt = occurredAt;
        }

    }
}

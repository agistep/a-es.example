package io.agistep.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.protobuf.Message;
import io.agistep.event.serialization.JsonObjectDeserializer;
import io.agistep.event.serialization.JsonSerializer;
import io.agistep.event.serialization.ProtocolBufferDeserializer;
import io.agistep.event.serialization.ProtocolBufferSerializer;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public final class ConvertUtil {
    public static String simpleEventConvert(Event e) {
        return EventToJson.convert(e);
    }

    public static Event simpleEventConvert(String str) {
        return JsonToEvent.convert(str);
    }

    public static String serializePayload(Object o) {
        return PayloadSerialization.convert(o);
    }

    public static Object deSerializePayload(Object o, String name) {
        return PayloadDeSerialization.convert(o, name);
    }

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
                    new JsonObjectDeserializer(),
                    new ProtocolBufferDeserializer(clazz)
            );

            Deserializer deserializer = deSerializers.stream()
                    .filter(s -> s.isSupport(serialized))
                    .findFirst()
                    .orElseThrow(UnsupportedOperationException::new);

            return deserializer.deserialize(String.valueOf(serialized).getBytes(StandardCharsets.UTF_8));
        }
    }

    static final class EventToJson {

        public static String convert(Event e) {
            ObjectMapper om = new ObjectMapper();
            om.registerModule(new JavaTimeModule());

            try {
                EventDTO eventDTO = new EventDTO();
                eventDTO.setId(e.getId());
                eventDTO.setName(e.getName());
                eventDTO.setSeq(e.getSeq());
                eventDTO.setAggregateId(e.getAggregateId());
                eventDTO.setOccurredAt(e.getOccurredAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

                Object payload = e.getPayload();

                ProtocolBufferSerializer serializer = new ProtocolBufferSerializer();
                if (serializer.isSupport(payload)) {
                    eventDTO.setPayload(new String(serializer.serialize(payload)));
                } else {
                    eventDTO.setPayload(String.valueOf(payload));
                }
                return om.writeValueAsString(eventDTO);
            } catch (JsonProcessingException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    static final class JsonToEvent {

        public static Event convert(String str) {
            ObjectMapper om = new ObjectMapper();
            try {
                EventDTO eventDTO = om.readValue(str, EventDTO.class);

                Class<?> clazz = Class.forName(eventDTO.getName());
                ProtocolBufferDeserializer deserialize = new ProtocolBufferDeserializer(clazz);
                Object payload;
                if (deserialize.isSupport(eventDTO.getPayload())) {
                    payload = deserialize.deserialize(eventDTO.getPayload().getBytes());
                } else {
                    payload = eventDTO.getPayload();
                }

                LocalDateTime occurredAt = LocalDateTime.parse(eventDTO.getOccurredAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                return EventSource.builder()
                        .id(eventDTO.getId())
                        .name(eventDTO.getName())
                        .seq(eventDTO.getSeq())
                        .aggregateId(eventDTO.getAggregateId())
                        .payload(payload)
                        .occurredAt(occurredAt).build();
            } catch (JsonProcessingException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
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

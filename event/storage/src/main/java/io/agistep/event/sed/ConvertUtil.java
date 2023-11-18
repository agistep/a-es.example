package io.agistep.event.sed;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.protobuf.Message;
import io.agistep.event.Event;
import io.agistep.event.Events;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public final class ConvertUtil {
    public static String convert(Event e) {
        return EventToJson.convert(e);
    }

    public static Event convert(String str) {
        return JsonToEvent.convert(str);
    }

    public static String convertPayload(Object o) {
        return PayloadSerialization.convert(o);
    }

    public static Message convertPayload(String o, String name) {
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

        public static Message convert(String serialized, String name) {
            Class<?> clazz;
            try {
                clazz = Class.forName(name);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            ProtocolBufferDeserializer protocolBufferDeserializer = new ProtocolBufferDeserializer(clazz);
            return (Message) protocolBufferDeserializer.deserialize(serialized.getBytes(StandardCharsets.UTF_8));
        }
    }

    static final class EventToJson {

        public static String convert(Event e) {
            try {
                EventDTO eventDTO = new EventDTO();
                eventDTO.setId(e.getId());
                eventDTO.setName(e.getName());
                eventDTO.setVersion(e.getSeq());
                eventDTO.setAggregateId(e.getAggregateId());
                eventDTO.setOccurredAt(e.getOccurredAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

                Object payload = e.getPayload();
                List<Serializer> serializers = List.of(
                        new JsonSerializer(),
                        new ProtocolBufferSerializer());
                Serializer serializer = serializers.stream()
                        .filter(s -> s.isSupport(payload))
                        .findFirst()
                        .orElseThrow(UnsupportedOperationException::new);
                eventDTO.setPayload(new String(serializer.serialize(payload)));


                ObjectMapper om = new ObjectMapper();
                om.registerModule(new JavaTimeModule());
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
                Object payload = deserialize.deserialize(eventDTO.getPayload().getBytes());
                LocalDateTime occurredAt = LocalDateTime.parse(eventDTO.getOccurredAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                return Events.builder()
                        .id(eventDTO.getId())
                        .name(eventDTO.getName())
                        .seq(eventDTO.getVersion())
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
        long version;
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

        public long getVersion() {
            return version;
        }

        public void setVersion(long version) {
            this.version = version;
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

package io.agistep.event.storages;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.Message;
import io.agistep.event.Event;
import io.agistep.event.EventSource;
import io.agistep.event.Serializer;
import io.agistep.event.serialization.NoOpSerializer;
import io.agistep.event.serialization.ProtocolBufferDeserializer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapEventStorage extends OptimisticLockingSupport {

    Map<Long, List<MapEvent>> events;
    private final Serializer serializer;

    public MapEventStorage() {
        this(new HashMap<>(), new NoOpSerializer());
    }

    public MapEventStorage(Map<Long, List<MapEvent>> events, Serializer serializer) {
        this.events = events;
        this.serializer = serializer;
    }

    @Override
    public void lockedSave(Event anEvent) {
        this.events.putIfAbsent(anEvent.getAggregateId(), new ArrayList<>());
        if (serializer instanceof NoOpSerializer) {
            Object payload1 = anEvent.getPayload();
            byte[] payload;
            if (payload1 instanceof Message) {
                payload = ((Message) payload1).toByteArray();
            } else {
                payload = String.valueOf(payload1).getBytes();
            }
            MapEvent mapEvent = new MapEvent(anEvent.getId(),
                    anEvent.getSeq(),
                    anEvent.getName(),
                    anEvent.getAggregateId(),
                    new String(payload),
                    anEvent.getOccurredAt()
            );
            this.events.get(anEvent.getAggregateId()).add(mapEvent);
        } else {
            MapEvent mapEvent = new MapEvent(anEvent.getId(),
                    anEvent.getSeq(),
                    anEvent.getName(),
                    anEvent.getAggregateId(),
                    new String(serializer.serialize(anEvent.getPayload())),
                    anEvent.getOccurredAt()
            );
            this.events.get(anEvent.getAggregateId()).add(mapEvent);
        }
    }

    @Override
    public List<Event> findByAggregate(long id) {
        return this.events.getOrDefault(id, List.of()).stream().map(a -> {
            Object o;
            try {
                if (serializer instanceof NoOpSerializer) {
                    Class<?> clazz = Class.forName(a.name);
                    ProtocolBufferDeserializer deserializer = new ProtocolBufferDeserializer(clazz);
                    if (deserializer.isSupport(a.payload)) {
                        o = deserializer.deserialize(a.payload.getBytes());
                    } else {
                        o = new String(a.getPayload());
                    }
                } else {
                    o = new String(a.getPayload());
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            return EventSource.builder()
                    .id(a.id)
                    .seq(a.seq)
                    .aggregateId(a.aggregateId)
                    .name(a.name)
                    .payload(o)
                    .occurredAt(a.occurredAt)
                    .build();
                }

        ).toList();
    }

    public Serializer getSerializer() {
        return serializer;
    }

    public static class MapEvent implements Event {
        private long id;
        private long seq;
        private String name;
        private long aggregateId;
        private String payload;
        private LocalDateTime occurredAt;

        public MapEvent(long id, long seq, String name, long aggregateId, String payload, LocalDateTime occurredAt) {
            this.id = id;
            this.seq = seq;
            this.name = name;
            this.aggregateId = aggregateId;
            this.payload = payload;
            this.occurredAt = occurredAt;
        }

        @Override
        public long getId() {
            return id;
        }

        @Override
        public long getSeq() {
            return seq;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public long getAggregateId() {
            return aggregateId;
        }

        @Override
        public String getPayload() {
            return payload;
        }

        @Override
        public LocalDateTime getOccurredAt() {
            return occurredAt;
        }
    }
}

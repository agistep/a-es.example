package io.agistep.event;

import io.agistep.aggregator.IdUtils;

import java.time.LocalDateTime;
import java.util.Objects;

import static org.hamcrest.CoreMatchers.*;
import static org.valid4j.Assertive.require;
import static org.valid4j.Validation.validate;

public class EventMaker {

    public static EventBuilder builder() {
        return new EventBuilder();
    }

    static Event make(long eventId, long aggregateId, long nextSeq, String eventName, LocalDateTime occurredAt, Object payload) {
        return EventBuilder.builder()
                .id(eventId)
                .name(eventName)
                .aggregateId(aggregateId)
                .seq(nextSeq)
                .payload(payload)
                .occurredAt(occurredAt)
                .build();
    }

    static Event make(Object aggregate, Object payload) {
        final long eventId = IdUtils.gen();
        final long aggregateId;
        final long nextSeq;

        if (IdUtils.notAssignedIdOf(aggregate)) {
            aggregateId = IdUtils.gen();
            nextSeq = EventSource.INITIAL_SEQ;
        } else {
            aggregateId = IdUtils.idOf(aggregate);
            nextSeq = nextSeq(aggregateId);
        }

        return EventBuilder.builder()
                .id(eventId)
                .name(payload.getClass().getName())
                .aggregateId(aggregateId)
                .seq(nextSeq)
                .payload(payload)
                .occurredAt(LocalDateTime.now())
                .build();
    }

    private static long nextSeq(Object aggregateId) {
        return ThreadLocalEventSeqHolder.instance().nextSeq((Long) aggregateId);
    }

    private static final class EventBuilder {
        private Long id;
        private String name;
        private Long seq;
        private Long aggregateId;
        private Object payload;
        private LocalDateTime occurredAt;

        private EventBuilder() {}

        public static EventBuilder builder() {
            return new EventBuilder();
        }

        public Event build() {
            validate(name.equals(payload.getClass().getName()), new IllegalArgumentException("Event name should be same with payload class name."));

            if(this.occurredAt == null) {
                this.occurredAt(LocalDateTime.now());
            }

            return new ObjectPayloadEnvelop(
                    require(id, is(not(nullValue()))),
                    name,
                    seq,
                    aggregateId,
                    payload,
                    occurredAt);
        }

        public EventBuilder name(String name) {
            this.name = name;
            return this;
        }

        public EventBuilder id(long id) {
            this.id = id;
            return this;
        }

        public EventBuilder seq(long seq) {
            this.seq = seq;
            return this;
        }

        public EventBuilder aggregateId(Long aggregateId) {
            this.aggregateId = aggregateId;
            return this;
        }

        public EventBuilder payload(Object payload) {
            this.payload = payload;
            return this;
        }

        public EventBuilder occurredAt(LocalDateTime occurredAt) {
            this.occurredAt = occurredAt;
            return this;
        }

    }

    private static class ObjectPayloadEnvelop implements Event {
        private final long id;
        private final String name;
        private final long seq;
        private final Long aggregateId;
        private final Object payload;
        private final LocalDateTime occurredAt;

        private ObjectPayloadEnvelop(long id, String name, long seq, Long aggregateId, Object payload, LocalDateTime occurredAt) {
            this.id = id;
            this.name = name;
            this.seq = seq;
            this.aggregateId = aggregateId;
            this.payload = payload;
            this.occurredAt = occurredAt;
        }

        @Override
        public long getId() {
            return id;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public long getSeq() {
            return seq;
        }

        @Override
        public long getAggregateId() {
            return aggregateId;
        }

        @Override
        public Object getPayload() {
            return payload;
        }

        @Override
        public LocalDateTime getOccurredAt() {
            return occurredAt;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ObjectPayloadEnvelop that = (ObjectPayloadEnvelop) o;
            return seq == that.seq && Objects.equals(aggregateId, that.aggregateId) && Objects.equals(name, that.name) && Objects.equals(payload, that.payload) && Objects.equals(occurredAt, that.occurredAt);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, seq, aggregateId, payload, occurredAt);
        }

        @Override
        public String toString() {
            return "ObjectPayloadEnvelop{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", seq=" + seq +
                    ", aggregateId=" + aggregateId +
                    ", payload=" + payload +
                    ", occurredAt=" + occurredAt +
                    '}';
        }
    }
}

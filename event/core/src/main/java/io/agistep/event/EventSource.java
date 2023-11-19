package io.agistep.event;

import io.agistep.aggregator.IdUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.CoreMatchers.*;
import static org.valid4j.Assertive.require;

public final class EventSource {
    public static final long INITIAL_SEQ = 0;
    public static HoldListener holdListener;
    public static ReorganizeListener reorganizeListener;

    private EventSource() {
        /* This is Utility */
    }

    public static EventBuilder builder() {
        return new EventBuilder();
    }

    public static void apply(Object aggregate, Object payload) {
        EventApplier.apply(aggregate, payload);
    }

    public static List<Event> getHoldEvents(Object aggregate) {
        return ThreadLocalEventHolder.instance().getEvents(aggregate);
    }

    public static long getLatestSeqOf(Object aggregate) {
        return getLatestSeqOf(IdUtils.idOf(aggregate));
    }

    public static long getLatestSeqOf(Long aggregateId) {
        return ThreadLocalEventSeqHolder.instance().getSeq(aggregateId);
    }

    public static void reorganize(Object aggregate, Event anEvent) {
        EventReorganizer.reorganize(aggregate, anEvent);
    }

    public static void reorganize(Object aggregate, Event[] events) {
        Arrays.stream(events)
                .forEach(e -> EventSource.reorganize(aggregate, e));
    }

    public static void clearAll() {
        ThreadLocalEventHolder.instance().clearAll();
    }

    public static void clear(Object aggregate) {
        ThreadLocalEventHolder.instance().clear(aggregate);
    }

    public static void setListener(Listener listener) {
        setListener((HoldListener)listener);
        setListener((ReorganizeListener)listener);
    }

    public static void setListener(HoldListener holdListener) {
        EventSource.holdListener = holdListener;
    }

    public static void setListener(ReorganizeListener reorganizeListener) {
        EventSource.reorganizeListener = reorganizeListener;
    }


    public static final class EventBuilder {
        private Long id;
        private String name;
        private Long seq;
        private Long aggregateId;
        private Object payload;
        private LocalDateTime occurredAt;

        private EventBuilder() {}

        public Event build() {
            if(this.occurredAt == null) {
                this.occurredAt(LocalDateTime.now());
            }

            //TODO required validation
            return new ObjectPayloadEnvelop(
                    require(id, is(not(nullValue()))),
                    name,
                    seq,
                    aggregateId,
                    payload,
                    occurredAt);
        }

        public EventBuilder id(long id) {
            this.id = id;
            return this;
        }

        public EventBuilder name(String name) {
            this.name = name;
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

package io.agistep.event;

import io.agistep.aggregator.IdUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.CoreMatchers.*;
import static org.valid4j.Assertive.require;

public final class Events {
    public static final long INITIAL_VERSION = 0;
    public static HoldListener holdListener;
    public static ReorganizeListener reorganizeListener;

    private Events() {
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

    public static long getLatestVersionOf(Object aggregate) {
        return getLatestVersionOf(IdUtils.idOf(aggregate));
    }

    public static long getLatestVersionOf(Long aggregateId) {
        return ThreadLocalEventVersionHolder.instance().getVersion(aggregateId);
    }

    public static void reorganize(Object aggregate, Event anEvent) {
        EventReorganizer.reorganize(aggregate, anEvent);
    }

    public static void reorganize(Object aggregate, Event[] events) {
        Arrays.stream(events)
                .forEach(e -> Events.reorganize(aggregate, e));
    }

    public static void clearAll() {
        ThreadLocalEventHolder.instance().clearAll();
    }

    public static void clear(Object aggregate) {
        ThreadLocalEventHolder.instance().clear(aggregate);
    }

    public static void setListener(HoldListener holdListener) {
        Events.holdListener = holdListener;
    }

    public static void setListener(ReorganizeListener reorganizeListener) {
        Events.reorganizeListener = reorganizeListener;
    }


    public static final class EventBuilder {
        private Long id;
        private String name;
        private Long version;
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
                    version,
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

        public EventBuilder version(long version) {
            this.version = version;
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
        private final long version;
        private final Long aggregateId;
        private final Object payload;
        private final LocalDateTime occurredAt;

        private ObjectPayloadEnvelop(long id, String name, long version, Long aggregateId, Object payload, LocalDateTime occurredAt) {
            this.id = id;
            this.name = name;
            this.version = version;
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
        public long getVersion() {
            return version;
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
            return version == that.version && aggregateId == that.aggregateId && Objects.equals(name, that.name) && Objects.equals(payload, that.payload) && Objects.equals(occurredAt, that.occurredAt);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, version, aggregateId, payload, occurredAt);
        }

        @Override
        public String toString() {
            return "ObjectPayloadEnvelop{" +
                    "name='" + name + '\'' +
                    ", version=" + version +
                    ", aggregateIdValue=" + aggregateId +
                    ", payload=" + payload +
                    ", occurredAt=" + occurredAt +
                    '}';
        }


    }


}

package io.agistep.event.test;

import io.agistep.event.Event;
import io.agistep.event.Events;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static io.agistep.event.Events.BEGIN_VERSION;
import static org.hamcrest.CoreMatchers.*;
import static org.valid4j.Validation.validate;

public final class EventListBuilder {

    public static EventListBuilder forTestWith(Object firstPayload) {
        return new EventListBuilder(getRandom(), firstPayload);
    }

    private static long getRandom() {
        return (long) (Math.random() % 10000);
    }

    public static EventListBuilder forTestWith(long aggregateId, Object firstPayload) {
        return new EventListBuilder(aggregateId, firstPayload);
    }

    private final long aggregateId;
    private final List<Object> payloads;

    public EventListBuilder(long aggregateId, Object firstPayload) {
        this.aggregateId = aggregateId;
        this.payloads = new ArrayList<>();
        addPayload(firstPayload);
    }

    public EventListBuilder next(Object payload) {
        addPayload(payload);
        return this;
    }

    private void addPayload(Object firstPayload) {
        Object payload = validate(firstPayload, is(not(nullValue())), IllegalArgumentException.class);
        this.payloads.add(payload);
    }

    public Event[] build() {
        AtomicLong eventId = new AtomicLong(getRandom());
        AtomicLong version = new AtomicLong(BEGIN_VERSION);
        return payloads.stream().map(p-> Events.builder()
                .id(eventId.getAndIncrement())
                .version(version.getAndIncrement())
                .aggregateId(this.aggregateId)
                .name(p.getClass().getName())
                .payload(p)
                .occurredAt(LocalDateTime.now())
                .build()).toList().toArray(new Event[0]);
    }
}

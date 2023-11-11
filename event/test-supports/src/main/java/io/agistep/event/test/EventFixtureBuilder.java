package io.agistep.event.test;

import io.agistep.event.Event;
import io.agistep.event.Events;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static io.agistep.event.Events.INITIAL_VERSION;
import static org.hamcrest.CoreMatchers.*;
import static org.valid4j.Validation.validate;

public final class EventFixtureBuilder {

    public static Event anEventWith(Object firstPayload) {
        return anEventWith(getRandom(), firstPayload);
    }

    public static Event anEventWith(long aggregateId, Object firstPayload) {
        return new EventFixtureBuilder(aggregateId, firstPayload).build()[0];
    }

    public static EventFixtureBuilder eventsWith(Object firstPayload) {
        return new EventFixtureBuilder(getRandom(), firstPayload);
    }

    private static long getRandom() {
        return (long) (Math.random() * 10000);
    }

    public static EventFixtureBuilder eventsWith(long aggregateId, Object firstPayload) {
        return new EventFixtureBuilder(aggregateId, firstPayload);
    }

    private final long aggregateId;
    private final List<Object> payloads;

    public EventFixtureBuilder(long aggregateId, Object firstPayload) {
        this.aggregateId = aggregateId;
        this.payloads = new ArrayList<>();
        addPayload(firstPayload);
    }

    public EventFixtureBuilder next(Object payload) {
        addPayload(payload);
        return this;
    }

    private void addPayload(Object firstPayload) {
        //TODO exception message
        Object payload = validate(firstPayload, is(not(nullValue())), IllegalArgumentException.class);
        this.payloads.add(payload);
    }

    public Event[] build() {
        AtomicLong eventId = new AtomicLong(getRandom());
        long latestVersion = Events.getLatestVersionOf(this.aggregateId);

        AtomicLong version = new AtomicLong(latestVersion ==-1 ? INITIAL_VERSION : latestVersion );
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

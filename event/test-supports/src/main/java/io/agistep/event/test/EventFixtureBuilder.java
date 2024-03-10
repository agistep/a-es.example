package io.agistep.event.test;

import io.agistep.aggregator.ConfigIdGenerator;
import io.agistep.event.Event;
import io.agistep.event.EventMaker;
import io.agistep.event.EventSource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static io.agistep.event.EventMaker.*;
import static io.agistep.event.EventSource.INITIAL_SEQ;
import static org.hamcrest.CoreMatchers.*;
import static org.valid4j.Validation.validate;

public final class EventFixtureBuilder {

    public static Event anEventWith(Object firstPayload) {
        return anEventWith(getId(), firstPayload);
    }

    public static Event anEventWith(long aggregateId, Object firstPayload) {
        return new EventFixtureBuilder(aggregateId, firstPayload).build()[0];
    }

    public static EventFixtureBuilder eventsWith(Object firstPayload) {
        return new EventFixtureBuilder(getId(), firstPayload);
    }

    private static long getId() {
        ConfigIdGenerator generator = new ConfigIdGenerator();
        return generator.gen();
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
        AtomicLong eventId = new AtomicLong(getId());
        long latestSeq = EventSource.getLatestSeqOf(this.aggregateId);
        AtomicLong seq = new AtomicLong(latestSeq ==-1 ? INITIAL_SEQ : latestSeq );

        return payloads.stream().map(p-> EventMaker.make(
                eventId(eventId.getAndIncrement()),
                aggregateId(this.aggregateId),
                seq(seq.getAndIncrement()),
                eventName(p.getClass().getName()),
                occurredAt(LocalDateTime.now()),
                payload(p))
        ).toList().toArray(new Event[0]);
    }

    public Event[] build(long beginSeq) {
        AtomicLong eventId = new AtomicLong(getId());
        AtomicLong seq = new AtomicLong(beginSeq == -1 ? INITIAL_SEQ : ++beginSeq);

        return payloads.stream().map(p-> EventMaker.make(
                eventId(eventId.getAndIncrement()),
                aggregateId(this.aggregateId),
                seq(seq.getAndIncrement()),
                eventName(p.getClass().getName()),
                occurredAt(LocalDateTime.now()),
                payload(p))
        ).toList().toArray(new Event[0]);
    }
}

package io.agistep.event.test;

import io.agistep.event.Event;

import java.util.function.Predicate;

public final class EventPredicates {

    private EventPredicates() {
    }

    public static Predicate<Event> equalsOccurredAt(Event expected) {
        return (e) -> e.getOccurredAt() == expected.getOccurredAt();
    }

    public static Predicate<Event> sameAggregateId(Event expected) {
        return (e) -> e.getAggregateId() == expected.getAggregateId();
    }

    public static Predicate<Event> sameId(Event expected) {
        return (e) -> e.getId() == expected.getId();
    }

    public static Predicate<Event> samePayload(Event expected) {
        return (e) -> e.getPayload() == expected.getPayload();
    }

    public static Predicate<Event> sameName(Event expected) {
        return (e) -> e.getName().equals(expected.getName());
    }

    public static Predicate<Event> sameVersion(Event expected) {
        return (e) -> e.getSeq() == expected.getSeq();
    }

}

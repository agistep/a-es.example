package io.agistep.event.test;

import io.agistep.event.Event;
import org.assertj.core.api.Condition;
import org.assertj.core.description.TextDescription;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;
import java.util.function.Predicate;

public final class EventPredicates {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private EventPredicates() {
    }

    public static Predicate<Event> equalsOccurredAt(Event expected) {
        return (e) -> {
            LocalDateTime occurredAt = e.getOccurredAt();
            LocalDateTime occurredAt1 = expected.getOccurredAt();
            return occurredAt.format(DATE_TIME_FORMATTER).equals(occurredAt1.format(DATE_TIME_FORMATTER));
        };
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

    public static Condition<Event> seqCondition(Event expected) {
        Function<Event, Object> extractor = Event::getSeq;
        return new EventCondition(expected,
                event -> extractor.apply(expected) == extractor.apply(event),
                "seq", extractor);
    }

    static class EventCondition extends Condition<Event> {

        final String fieldName;
        final Function<Event, Object> extractor;
        final Event expected;

        EventCondition(Event expected, Predicate<Event> predicate, String fieldName, Function<Event, Object> extractor) {
            super(predicate, "");
            this.fieldName = fieldName;
            this.extractor = extractor;
            this.expected = expected;
        }

        @Override
        public boolean matches(Event actual) {
            boolean matches = super.matches(actual);
            String pDescription = "Expected %s is %s, But Actual is %s.";
            TextDescription description = new TextDescription(
                    pDescription, fieldName, extractor.apply(expected), extractor.apply(actual));
            describedAs(description);
            return matches;
        }
    }


}

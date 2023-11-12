package io.agistep.event.test;

import io.agistep.event.Event;
import org.assertj.core.api.Condition;
import org.assertj.core.description.TextDescription;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;
import java.util.function.Predicate;

public final class EventMatchConditions {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private EventMatchConditions() {
    }

    public static Condition<Event> idCondition(Event expected) {
        Function<Event, Object> extractor = Event::getId;
        String fieldName = "id";
        return new EventCondition(expected,
                event -> extractor.apply(expected) == extractor.apply(event),
                fieldName, extractor);
    }

    public static Condition<Event> aggregateIdCondition(Event expected) {
        Function<Event, Object> extractor = Event::getAggregateId;
        String fieldName = "aggregateId";
        return new EventCondition(expected,
                event -> extractor.apply(expected).equals(extractor.apply(event)),
                fieldName, extractor);
    }

    public static Condition<Event> seqCondition(Event expected) {
        Function<Event, Object> extractor = Event::getSeq;
        String fieldName = "seq";
        return new EventCondition(expected,
                event -> extractor.apply(expected) == extractor.apply(event),
                fieldName, extractor);
    }

    public static Condition<Event> nameCondition(Event expected) {
        Function<Event, Object> extractor = Event::getName;
        String fieldName = "name";
        return new EventCondition(expected,
                event -> extractor.apply(expected) == extractor.apply(event),
                fieldName, extractor);
    }

    public static Condition<Event> payloadCondition(Event expected) {
        Function<Event, Object> extractor = Event::getPayload;
        String fieldName = "payload";
        return new EventCondition(expected,
                event -> extractor.apply(expected) == extractor.apply(event),
                fieldName, extractor);
    }

    public static Condition<Event> occurredAtCondition(Event expected) {
        Function<Event, Object> extractor = Event::getOccurredAt;
        String fieldName = "occurredAt";
        return new EventCondition(expected,
                event -> {
                    LocalDateTime occurredAt = event.getOccurredAt();
                    LocalDateTime occurredAt1 = expected.getOccurredAt();
                    return occurredAt.format(DATE_TIME_FORMATTER).equals(occurredAt1.format(DATE_TIME_FORMATTER));
                },
                fieldName, extractor);
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

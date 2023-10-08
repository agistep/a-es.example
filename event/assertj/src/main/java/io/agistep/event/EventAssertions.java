package io.agistep.event;


import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;

import static io.agistep.event.Events.BEGIN_ORDER;
import static org.assertj.core.api.Assertions.assertThat;

public final class EventAssertions {

	public static void assertThatOccurredExactlyOnes(Object aggregate, Object payload) {
		long aggregateIdValue = AggregateSupports.getId(aggregate);
		assertThatOccurredExactlyOnes(aggregate, Events.builder()
				.name(payload.getClass().getName())
				.order(EventList.instance().getLatestOrderOf(aggregate))
				.aggregateIdValue(aggregateIdValue)
				.payload(payload)
				.occurredAt(LocalDateTime.now())
				.build());
	}

	public static void assertThatOccurredExactlyOnes(Object aggregate, Event expected) {
		List<Event> eventList = EventList.instance().occurredListBy(aggregate);
		long occurredCount = eventList.size();
		assertThat(occurredCount).describedAs("Expected count of occurred is 1").isEqualTo(1);
		Event actual = eventList.get(0);
		assertThat(actual.getName()).describedAs("Event name is invalid").isEqualTo(expected.getName());
		assertThat(actual.getOrder() ).describedAs("Event Order is Invalid").isEqualTo(expected.getOrder());
		assertThat(actual.getAggregateIdValue()).describedAs("Aggregate ID is Invalid").isEqualTo(expected.getAggregateIdValue());
		assertThat(actual.getPayload()).describedAs("Payload is Invalid").isEqualTo(expected.getPayload());
		long until = actual.getOccurredAt().until(expected.getOccurredAt(), ChronoUnit.MILLIS);
		assertThat(1000 > until).describedAs("Occurred At is Invalid").isTrue();
	}

	public static void assertThatDoesNotOccurAnEventBy(Object aggregate) {
		assertThat(EventList.instance().occurredListBy(aggregate)).isEmpty();
	}
}

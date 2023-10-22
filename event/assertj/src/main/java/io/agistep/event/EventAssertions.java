package io.agistep.event;


import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public final class EventAssertions {

	public static void assertThatOccurredExactlyOnes(Object aggregate, Object payload) {
		long aggregateIdValue = AggregateIdUtils.getIdFrom(aggregate);
		assertThatOccurredExactlyOnes(aggregate, new EventBuilder()
				.name(payload.getClass().getName())
				.version(EventHolder.instance().getLatestVersionOf(aggregate))
				.aggregateId(aggregateIdValue)
				.payload(payload)
				.occurredAt(LocalDateTime.now())
				.build());
	}

	public static void assertThatOccurredExactlyOnes(Object aggregate, Event expected) {
		List<Event> eventList = EventHolder.instance().getEvents(aggregate);
		long occurredCount = eventList.size();
		assertThat(occurredCount).describedAs("Expected count of occurred is 1").isEqualTo(1);

		Event actual = eventList.get(0);
		assertEquals(actual, expected);
	}

	public static void assertThatOccurredExactly (Object aggregate, Event... expectedList) {
		List<Event> eventList = EventHolder.instance().getEvents(aggregate);
		long occurredCount = eventList.size();
		assertThat(occurredCount).describedAs("Count of occurred is "+ eventList.size()).isEqualTo(expectedList.length);

		for (int i = 0; i < eventList.size(); i++) {
			Event actual = eventList.get(i);
			Event expected= expectedList[i];

			assertEquals(actual, expected);
		}

	}

	private static void assertEquals(Event actual, Event expected) {
		assertThat(actual.getName()).describedAs("Event name is invalid").isEqualTo(expected.getName());
		assertThat(actual.getVersion() ).describedAs("Event Version is Invalid").isEqualTo(expected.getVersion());
		assertThat(actual.getAggregateId()).describedAs("Aggregate ID is Invalid").isEqualTo(expected.getAggregateId());
		assertThat(actual.getPayload()).describedAs("Payload is Invalid").isEqualTo(expected.getPayload());
		long until = actual.getOccurredAt().until(expected.getOccurredAt(), ChronoUnit.MILLIS);
		assertThat(1000 > until).describedAs("Occurred At is Invalid").isTrue();
	}

	public static void assertThatDoesNotOccurAnEventBy(Object aggregate) {
		assertThat(EventHolder.instance().getEvents(aggregate)).isEmpty();
	}
}

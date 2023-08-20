package io.agistep.event;


import java.time.LocalDateTime;
import java.util.List;

import static io.agistep.event.Events.BEGIN_ORDER;
import static org.assertj.core.api.Assertions.assertThat;

public final class EventAssertions {

	public static void assertThatOccurredExactlyOnes(Object aggregate, Object payload) {
		long aggregateIdValue = AggregateSupports.getId(aggregate);
		assertThatOccurredExactlyOnes(aggregate, Events.builder()
				.name(payload.getClass().getName())
				.order(BEGIN_ORDER) //TODO 이전 order 를 알아야한다.
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
		assertThat(actual.getOccurredAt()).describedAs("Occurred At is Invalid").isEqualToIgnoringNanos(expected.getOccurredAt());
	}

	public static void assertThatDoesNotOccurAnEventBy(Object aggregate) {
		assertThat(EventList.instance().occurredListBy(aggregate)).isEmpty();
	}
}

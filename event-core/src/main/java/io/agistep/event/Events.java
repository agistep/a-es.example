package io.agistep.event;

import java.time.LocalDateTime;
import java.util.Arrays;

public final class Events {

	public static final long BEGIN_ORDER = 1L;

	static EventBuilder builder() {
		return new EventBuilder();
	}

	static Event create(long aggregateIdValue, Object payload) {
		return Events.builder()
				.name(payload.getClass().getName())
				.order(BEGIN_ORDER) //TODO fix 이전 order 를 알아야한다.
				.aggregateIdValue(aggregateIdValue)
				.payload(payload)
				.occurredAt(LocalDateTime.now())
				.build();
	}


    // FOR TEST
	public static Event[] events(long aggregateIdValue, Object... payload) {
		return Arrays.stream(payload).map(p -> Events.mock(aggregateIdValue, p)).toArray(Event[]::new);
	}

	// FOR TEST
	public static Event mock(long aggregateIdValue, Object payload) {
		return Events.builder()
				.name(payload.getClass().getName())
				.order(BEGIN_ORDER) //TODO 이전 order 를 알아야한다.
				.aggregateIdValue(aggregateIdValue)
				.payload(payload)
				.occurredAt(LocalDateTime.now())
				.build();
	}
}

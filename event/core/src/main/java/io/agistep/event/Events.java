package io.agistep.event;

import java.time.LocalDateTime;

public final class Events {

	public static final long BEGIN_ORDER = 1L;
	public static final int INITIAL_ORDER = 0;

	static EventBuilder builder() {
		return new EventBuilder();
	}

	static Event create(long aggregateIdValue, Object payload) {
		return Events.builder()
				.name(payload.getClass().getName())
				.order(getCurrentOrder()+1)
				.aggregateIdValue(aggregateIdValue)
				.payload(payload)
				.occurredAt(LocalDateTime.now())
				.build();
	}

	private static long getCurrentOrder() {
		return getInitialOrder();
	}

	private static long getInitialOrder() {
		return INITIAL_ORDER;
	}

}

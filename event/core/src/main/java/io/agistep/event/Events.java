package io.agistep.event;

import java.time.LocalDateTime;

public final class Events {

	static EventBuilder builder() {
		return new EventBuilder();
	}

	public static Event create(Object aggregate, Object payload) {
		return Events.builder()
				.name(payload.getClass().getName())
				.aggregate(aggregate)
				.payload(payload)
				.occurredAt(LocalDateTime.now())
				.build();
	}

}

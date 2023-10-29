package io.agistep.event;

import java.time.LocalDateTime;

public class TestEvents {
	public static Event anEvent(long aggregateId, long version, Object payload) {
		return new EventBuilder()
				.name(payload.getClass().getName())
				.version(version)
				.aggregateId(aggregateId)
				.payload(payload)
				.occurredAt(LocalDateTime.now())
				.build();
	}

	public static Event[] anEvent(long aggregateId, Object ... payloads) {

		Event[] ret = new Event[payloads.length];
		for (int i = 0; i < payloads.length; i++) {
			Object payload = payloads[i];
			Event e = new EventBuilder()
					.name(payload.getClass().getName())
					.version(i)
					.aggregateId(aggregateId)
					.payload(payload)
					.occurredAt(LocalDateTime.now())
					.build();
			ret[i]= e;
		}
		return ret;
	}

}

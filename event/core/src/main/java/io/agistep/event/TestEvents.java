package io.agistep.event;

import java.time.LocalDateTime;

public class TestEvents {
	public static Event[] events(long aggregateIdValue, Object ... payloads) {

		Event[] ret = new Event[payloads.length];
		for (int i = 0; i < payloads.length; i++) {
			Object payload = payloads[i];
			Event e = new EventBuilder()
					.name(payload.getClass().getName())
					.version(i)
					.aggregateIdValue(aggregateIdValue)
					.payload(payload)
					.occurredAt(LocalDateTime.now())
					.build();
			ret[i]= e;
		}
		return ret;
	}

}

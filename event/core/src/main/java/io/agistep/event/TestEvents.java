package io.agistep.event;

import java.time.LocalDateTime;
import java.util.Arrays;

import static io.agistep.event.Events.BEGIN_ORDER;

public class TestEvents {
	public static Event[] events(long aggregateIdValue, Object ... payloads) {

		Event[] ret = new Event[payloads.length];
		for (int i = 0; i < payloads.length; i++) {
			Object payload = payloads[i];
			Event e = Events.builder()
					.name(payload.getClass().getName())
					.order(i)
					.aggregateIdValue(aggregateIdValue)
					.payload(payload)
					.occurredAt(LocalDateTime.now())
					.build();
			ret[i]= e;
		}
		return ret;
	}

}

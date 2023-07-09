package io.agistep.event;

import java.time.LocalDateTime;
import java.util.Arrays;

import static io.agistep.event.Events.BEGIN_ORDER;

public class TestEvents {
	public static Event[] events(long aggregateIdValue, Object[] payload) {
		return Arrays.stream(payload).map(p -> Events.builder()
				.name(p.getClass().getName())
				.order(BEGIN_ORDER) //TODO 이전 order 를 알아야한다.
				.aggregateIdValue(aggregateIdValue)
				.payload(p)
				.occurredAt(LocalDateTime.now())
				.build()).toArray(Event[]::new);
	}

}

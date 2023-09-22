package io.agistep.event;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

public final class Events {

	public static final long BEGIN_ORDER = 1L;
	public static final int INITIAL_ORDER = 0;

	static EventBuilder builder() {
		return new EventBuilder();
	}

	private static long nextOrder(long aggregateIdValue, Map<Long, Long> ids) {
		long order;
        if (hasNotBeenPublishedBy(aggregateIdValue, ids)) {
			order = initOrder();
		} else {
			order = previousOrder(aggregateIdValue,ids) + 1;
        }
		ids.put(aggregateIdValue, order);
		return order;
	}

	private static long initOrder() {
		return (long) INITIAL_ORDER + 1;
	}

	private static boolean hasNotBeenPublishedBy(long aggregateIdValue,Map<Long, Long> ids) {
		return Objects.isNull(previousOrder(aggregateIdValue,ids));
	}

	private static Long previousOrder(long aggregateIdValue, Map<Long, Long> ids) {
		return ids.get(aggregateIdValue);
	}

	public static Event create(long aggregateIdValue, Object payload, Map<Long, Long> ids) {
		long order = nextOrder(aggregateIdValue, ids);
		return Events.builder()
				.name(payload.getClass().getName())
				.order(order)
				.aggregateIdValue(aggregateIdValue)
				.payload(payload)
				.occurredAt(LocalDateTime.now())
				.build();
	}
}

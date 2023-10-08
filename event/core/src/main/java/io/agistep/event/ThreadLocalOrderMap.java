package io.agistep.event;

import io.agistep.identity.IdentityValueProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class ThreadLocalOrderMap{

	public static final long BEGIN_ORDER = 0;

	private final static ThreadLocal<Map<Long,Long>> changes = new ThreadLocal<>();

	public static ThreadLocalOrderMap instance() {
		return new ThreadLocalOrderMap();
	}

	private ThreadLocalOrderMap() {
		// Do Nothing
	}

	public void setOrder(Object aggregate, Event anEvent) {
		Long aggregateId = AggregateSupports.getId(aggregate);

		if (isNotInit(aggregateId)) {
			init(aggregate);
		}
		long order = anEvent.getOrder();
		if (Objects.isNull(changes.get())) {
			changes.set(new HashMap<>());
		}
		changes.get().put(aggregateId, order);
	}

	private void init(Object aggregate) {
		Long aggregateId = AggregateSupports.getId(aggregate);
		Map<Long, Long> map = new HashMap<>();
		map.put(aggregateId, 0L);
		changes.set(map);
	}

	private boolean isNotInit(Long aggregateId) {
		if (Objects.isNull(changes.get())) {
			return true;
		}
		return Optional.ofNullable(changes.get().get(aggregateId)).isEmpty();
	}

	public Long setOrder(Object aggregate) {
		if (Objects.isNull(changes.get())) {
			changes.set(new HashMap<>());
		}

		final long aggregateIdValue = AggregateSupports.getId(aggregate) == -1 ?
				IdentityValueProvider.instance().newLong() : AggregateSupports.getId(aggregate);
		if (!changes.get().containsKey(aggregateIdValue)) {
			changes.get().put(aggregateIdValue, BEGIN_ORDER);
		} else {
			changes.get().put(aggregateIdValue, changes.get().get(aggregateIdValue) + 1);
		}

		return changes.get().get(aggregateIdValue);
	}

	public void clear() {
		changes.remove();
	}
}

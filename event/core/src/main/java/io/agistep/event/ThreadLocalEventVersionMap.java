package io.agistep.event;

import io.agistep.identity.IdentityValueProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class ThreadLocalEventVersionMap {

	public static final long BEGIN_VERSION = 0;

	private final static ThreadLocal<Map<Long,Long>> changes = new ThreadLocal<>();

	public static ThreadLocalEventVersionMap instance() {
		return new ThreadLocalEventVersionMap();
	}

	private ThreadLocalEventVersionMap() {
		// Do Nothing
	}

	public void setVersion(Object aggregate, Event anEvent) {
		Long aggregateId = AggregateSupports.getId(aggregate);

		if (isNotInit(aggregateId)) {
			init(aggregate);
		}
		long version = anEvent.getVersion();
		if (Objects.isNull(changes.get())) {
			changes.set(new HashMap<>());
		}
		changes.get().put(aggregateId, version);
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

	public Long setVersion(Object aggregate) {
		if (Objects.isNull(changes.get())) {
			changes.set(new HashMap<>());
		}

		final long aggregateIdValue = AggregateSupports.getId(aggregate) == -1 ?
				IdentityValueProvider.instance().newLong() : AggregateSupports.getId(aggregate);
		if (!changes.get().containsKey(aggregateIdValue)) {
			changes.get().put(aggregateIdValue, BEGIN_VERSION);
		} else {
			changes.get().put(aggregateIdValue, changes.get().get(aggregateIdValue) + 1);
		}

		return changes.get().get(aggregateIdValue);
	}

	public void clear() {
		changes.remove();
	}
}

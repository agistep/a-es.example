package io.agistep.event;

import io.agistep.identity.IdentityValueProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class ThreadLocalEventVersionHolder {

	public static final long BEGIN_VERSION = 0;

	private final static ThreadLocal<Map<Long,Long>> changes = new ThreadLocal<>();

	public static ThreadLocalEventVersionHolder instance() {
		return new ThreadLocalEventVersionHolder();
	}

	private ThreadLocalEventVersionHolder() {
		// Do Nothing
	}

	public void setVersion(Object aggregate, Event anEvent) {
		Long aggregateId = AggregateIdUtils.getIdFrom(aggregate);

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
		Long aggregateId = AggregateIdUtils.getIdFrom(aggregate);
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

		final long aggregateIdValue = AggregateIdUtils.getIdFrom(aggregate) == -1 ?
				IdentityValueProvider.instance().newLong() : AggregateIdUtils.getIdFrom(aggregate);
		if (!changes.get().containsKey(aggregateIdValue)) {
			changes.get().put(aggregateIdValue, BEGIN_VERSION);
		} else {
			changes.get().put(aggregateIdValue, changes.get().get(aggregateIdValue) + 1);
		}

		return changes.get().get(aggregateIdValue);
	}

	public void clearAll() {
		changes.remove();
	}

	public void clear(Object aggregate) {
		//TODO
		changes.remove();
	}
}

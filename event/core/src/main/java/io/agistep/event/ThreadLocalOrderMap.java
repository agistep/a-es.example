package io.agistep.event;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class ThreadLocalOrderMap{

	private final static ThreadLocal<Map<Long,Long>> changes = new ThreadLocal<>();

	public static ThreadLocalOrderMap instance() {
		return new ThreadLocalOrderMap();
	}

	private ThreadLocalOrderMap() {
		// Do Nothing
	}

	public void init(Object aggregate) {
		Long aggregateId = AggregateSupports.getId(aggregate);
		Map<Long, Long> map = new HashMap<>();
		map.put(aggregateId, 0L);
		changes.set(map);
	}

	public Map<Long, Long> getMap(Object aggregate) {
		return changes.get();
	}

	public Long getOrder(long aggregateId) {
		Map<Long, Long> longLongMap = changes.get();
		return longLongMap.get(aggregateId);
	}

	public void putOrder(long aggregateIdValue, long order) {
		if (Objects.isNull(changes.get())) {
			changes.set(new HashMap<>());
		}
		Map<Long, Long> longLongMap = changes.get();
		longLongMap.put(aggregateIdValue, order);
	}

	public void putOrder(Object aggregate, Event anEvent) {
		Long aggregateId = AggregateSupports.getId(aggregate);

		if (isNotInit(aggregateId)) {
			init(aggregate);
		}
		putOrder(aggregateId, anEvent.getOrder());
	}

	private boolean isNotInit(Long aggregateId) {
		if (Objects.isNull(changes.get())) {
			return true;
		}
		return Optional.ofNullable(this.getOrder(aggregateId)).isEmpty();
	}

	public Long putOrder(long aggregateIdValue) {
		if (Objects.isNull(changes.get())) {
			changes.set(new HashMap<>());
		}

		if (!changes.get().containsKey(aggregateIdValue)) {
			changes.get().put(aggregateIdValue, 0L);
		} else {
			changes.get().put(aggregateIdValue, changes.get().get(aggregateIdValue) + 1);
		}

		return changes.get().get(aggregateIdValue);
	}

	public void clear() {
		changes.remove();
	}
}

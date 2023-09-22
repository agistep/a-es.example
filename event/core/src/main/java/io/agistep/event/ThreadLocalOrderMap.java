package io.agistep.event;

import java.util.HashMap;
import java.util.Map;

class ThreadLocalOrderMap{

	private final static ThreadLocal<Map<Long,Long>> changes = new ThreadLocal<>();

	static ThreadLocalOrderMap instance() {
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
		Long aggregateId = AggregateSupports.getId(aggregate);
		return changes.get();
	}
}

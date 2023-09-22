package io.agistep.event;

import java.util.HashMap;
import java.util.Map;

public interface EventApplier {

	static EventApplier instance() {
		return EventApplierFacade.instance();
	}

	default Map<Long, Long> init(Object aggregate, Object payload) {
		Map<Long, Long> aggregateOrderMap = new HashMap<>();
		EventApplier.instance().apply(aggregate, payload, aggregateOrderMap);

		ThreadLocalOrderMap.instance().init(aggregate);

		return aggregateOrderMap;
	}

	@Deprecated
	void apply(Object aggregate, Object payload, Map<Long, Long> aggregateOrderMap);

	default void apply(Object aggregate, Object payload) {
		throw new UnsupportedOperationException("여기로 가야함 ");
	}


}

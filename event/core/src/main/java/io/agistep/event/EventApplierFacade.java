package io.agistep.event;

import io.agistep.identity.IdentityValueProvider;

import java.util.Map;

class EventApplierFacade implements EventApplier {

	static EventApplier instance() {
		return new EventApplierFacade();
	}

	private EventApplierFacade() {
	}


	@Override
	public void apply(Object aggregate, Object payload, Map<Long, Long> aggregateOrderMap) {

		Map<Long, Long> map = ThreadLocalOrderMap.instance().getMap(aggregate);

		Event anEvent = getEvent(aggregate, payload, aggregateOrderMap);

		EventList.instance().occurs(anEvent);
		EventReorganizer.reorganize(aggregate, anEvent);
	}

	private Event getEvent(Object aggregate, Object payload, Map<Long, Long> ids) {
		long id = AggregateSupports.getId(aggregate) == -1 ?
				IdentityValueProvider.instance().newLong() :  AggregateSupports.getId(aggregate);

		return Events.create(id, payload, ids);
	}
}

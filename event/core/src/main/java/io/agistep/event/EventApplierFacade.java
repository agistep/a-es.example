package io.agistep.event;

import io.agistep.identity.IdentityValueProvider;

import java.util.HashMap;

class EventApplierFacade implements EventApplier {

	static EventApplier instance() {
		return new EventApplierFacade();
	}

	private EventApplierFacade() {
	}

	@Override
	public void apply(Object aggregate, Object payload) {
		HashMap<Long, Long> ids1 = new HashMap<>();
		Event anEvent = getEvent(aggregate, payload, ids1);

		EventList.instance().occurs(anEvent);
		EventReorganizer.reorganize(aggregate, anEvent);
	}

	private Event getEvent(Object aggregate, Object payload, HashMap<Long, Long> ids1) {
		long id = AggregateSupports.getId(aggregate) == -1 ?
				IdentityValueProvider.instance().newLong() :  AggregateSupports.getId(aggregate);

		return Events.create(id, payload, ids1);
	}
}

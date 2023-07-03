package io.agistep.event;

import io.agistep.identity.IdentityValueProvider;

class EventApplierFacade implements EventApplier {

	static EventApplier instance() {
		return new EventApplierFacade();
	}

	private EventApplierFacade() {
	}

	@Override
	public void apply(Object aggregate, Object payload) {
		long id = AggregateSupports.getId(aggregate);
		Event anEvent;
		if ((-1 == id)) {
			long idValue = IdentityValueProvider.instance().newLong();
			anEvent = Events.begin(idValue, payload);
		} else {
			anEvent = Events.occurs(id, payload);
		}

		EventList.instance().occurs(anEvent);
		EventReorganizer.reorganize(aggregate, anEvent);
	}

}

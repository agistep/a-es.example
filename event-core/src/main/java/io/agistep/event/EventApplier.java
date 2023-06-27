package io.agistep.event;

import io.agistep.identity.IdentityValueProvider;

public interface EventApplier {
	static EventApplier instance() {
		return SimpleEventApplier.instance();
	}


	default void apply(Object aggregate, Object payload) {
		long id = AggregateSupports.getId(aggregate);
		Event event;
		if ((-1 == id)) {
			long idValue = IdentityValueProvider.instance().newLong();
			event = Events.begin(idValue, payload);
		} else {
			event = Events.occurs(id, payload);
		}

		apply(aggregate, event);
	}

	default void apply(Object aggregate, Event anEvent) {
		occurred(anEvent);
		replay(aggregate, anEvent);
	}

	void replay(Object aggregate, Event anEvent);

	void occurred(Event anEvent);
}

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
        Event anEvent = createEvent(aggregate, payload);

        EventList.instance().occurs(anEvent);
        EventReorganizer.reorganize(aggregate, anEvent);
    }

    private Event createEvent(Object aggregate, Object payload) {
        long id = AggregateSupports.getId(aggregate) == -1 ?
                IdentityValueProvider.instance().newLong() : AggregateSupports.getId(aggregate);

        return Events.create(id, payload);
    }
}

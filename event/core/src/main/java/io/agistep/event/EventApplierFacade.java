package io.agistep.event;

class EventApplierFacade implements EventApplier {

    static EventApplier instance() {
        return new EventApplierFacade();
    }

    private EventApplierFacade() {
    }

    @Override
    public void apply(Object aggregate, Object payload) {
        Event anEvent = Events.create(aggregate, payload);

        EventList.instance().occurs(anEvent);
        EventReorganizer.reorganize(aggregate, anEvent);
    }

}

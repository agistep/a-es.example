package io.agistep.event;

import java.time.LocalDateTime;

class EventApplierImpl implements EventApplier {

    static EventApplier instance() {
        return new EventApplierImpl();
    }

    private EventApplierImpl() {
    }

    @Override
    public void apply(Object aggregate, Object payload) {
        Event anEvent = new EventBuilder()
                .name(payload.getClass().getName())
                .aggregate(aggregate)
                .payload(payload)
                .occurredAt(LocalDateTime.now())
                .build();

        EventList.instance().occurs(anEvent);
        EventReorganizer.reorganize(aggregate, anEvent);
    }

}

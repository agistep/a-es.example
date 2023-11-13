package io.agistep.event.test;

import io.agistep.event.Event;

import java.util.ArrayList;
import java.util.List;

class HoldingEventLogger {

    private static final ThreadLocal<List<Event>> logs = ThreadLocal.withInitial(ArrayList::new);

    static HoldingEventLogger init() {
        return new HoldingEventLogger();
    }


    Event get(int index) {
        return getEvents().get(index);
    }

    private List<Event> getEvents() {
        return this.logs.get();
    }

    int size() {
        return getEvents().size();
    }

    Event[] getAll() {
        return getEvents().toArray(new Event[0]);
    }

    void add(Event anEvent) {
        getEvents().add(anEvent);
    }

    void clear() {
        logs.remove();
    }
}

package io.agistep.event.test;

import io.agistep.event.Event;
import io.agistep.event.ListenerSupport;

import java.util.ArrayList;
import java.util.List;

public class TestEventLogger extends ListenerSupport {

    private final List<Event> log;

    TestEventLogger() {
        this.log = new ArrayList<>();
    }

    @Override
    public void afterHold(Event anEvent) {
        this.log.add(anEvent);
    }

    public Event get(int index) {
        return this.log.get(index);
    }

    public int size() {
        return this.log.size();
    }

    public Event[] getAll() {
        return this.log.toArray(new Event[0]);
    }
}

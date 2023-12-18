package io.agistep.event.test;

import io.agistep.event.Event;
import io.agistep.event.ListenerSupport;

public class HoldingEventListener extends ListenerSupport {

    private final HoldingEventLogger log;

    public HoldingEventListener() {
        this.log = HoldingEventLogger.init();
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
        return this.log.getAll();
    }

    public void clear() {
        this.log.clear();
    }
}

package io.agistep.event;

import io.agistep.aggregator.Aggregate;

public abstract class ListenerSupport implements Listener {
    @Override
    public void beforeHold(Event anEvent) {

    }

    @Override
    public void afterHold(Event anEvent) {

    }

    @Override
    public void beforeReplay(Aggregate aggregate, Event event) {

    }

    @Override
    public void afterReplay(Aggregate aggregate, Event event) {

    }
}

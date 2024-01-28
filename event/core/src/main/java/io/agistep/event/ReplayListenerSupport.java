package io.agistep.event;

import io.agistep.aggregator.Aggregate;

public abstract class ReplayListenerSupport implements ReplayListener {
    @Override
    public void beforeReplay(Aggregate aggregate, Event event) {

    }

    @Override
    public void afterReplay(Aggregate aggregate, Event event) {

    }
}

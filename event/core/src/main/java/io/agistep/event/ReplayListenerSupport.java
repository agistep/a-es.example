package io.agistep.event;

public abstract class ReplayListenerSupport implements ReplayListener {
    @Override
    public void beforeReplay(Object aggregate, Event event) {

    }

    @Override
    public void afterReplay(Object aggregate, Event event) {

    }
}

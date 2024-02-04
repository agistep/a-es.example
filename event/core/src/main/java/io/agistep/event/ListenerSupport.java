package io.agistep.event;

public abstract class ListenerSupport implements Listener {
    @Override
    public void beforeHold(Event anEvent) {

    }

    @Override
    public void afterHold(Event anEvent) {

    }

    @Override
    public void beforeReplay(Object aggregate, Event event) {

    }

    @Override
    public void afterReplay(Object aggregate, Event event) {

    }
}

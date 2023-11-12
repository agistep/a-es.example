package io.agistep.event;

public abstract class ListenerSupport implements Listener {
    @Override
    public void beforeHold(Event anEvent) {

    }

    @Override
    public void afterHold(Event anEvent) {

    }

    @Override
    public void beforeReorganize(Object aggregate, Event event) {

    }

    @Override
    public void afterReorganize(Object aggregate, Event event) {

    }
}

package io.agistep.event;

public interface HoldListener {
    void beforeHold(Event anEvent);

    void afterHold(Event anEvent);
}

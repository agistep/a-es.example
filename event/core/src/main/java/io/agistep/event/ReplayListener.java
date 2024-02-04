package io.agistep.event;

public interface ReplayListener {
    void beforeReplay(Object aggregate, Event event);
    void afterReplay(Object aggregate, Event event);
}

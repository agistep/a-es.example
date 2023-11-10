package io.agistep.event;

public interface ReorganizeListener {
    void beforeReorganize(Object aggregate, Event event);
    void afterReorganize(Object aggregate, Event event);
}

package io.agistep.event;

import io.agistep.aggregator.Aggregate;

public interface ReplayListener {
    void beforeReplay(Aggregate aggregate, Event event);
    void afterReplay(Aggregate aggregate, Event event);
}

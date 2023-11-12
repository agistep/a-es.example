package io.agistep.event.storages;

import io.agistep.event.Event;

import java.util.List;

abstract class UnsupportedEventStorage implements EventStorage {
    @Override
    public void save(Event anEvents) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Event> findByAggregate(long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long findLatestVersionOfAggregate(long id) {
        throw new UnsupportedOperationException();
    }
}

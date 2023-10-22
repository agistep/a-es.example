package io.agistep.event.storages;

import io.agistep.event.Event;

import java.util.List;

abstract class UnsupportedEventStorage implements EventStorage {
    @Override
    public void save(List<Event> events) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Event> findAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Event> findById(long id) {
        throw new UnsupportedOperationException();
    }
}

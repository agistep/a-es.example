package io.agistep.event.storages;

import io.agistep.event.Event;

import java.util.List;

public interface EventStorage {

    void save(List<Event> events);

    List<Event> findAll();

    List<Event> findById(long id);
}

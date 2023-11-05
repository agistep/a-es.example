package io.agistep.event.storages;

import io.agistep.event.Event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class MapEventStorage implements EventStorage {

    Map<Long, List<Event>> events;

    public MapEventStorage(Map<Long, List<Event>> events) {
        this.events = events;
    }

    @Override
    public void save(List<Event> events) {
        events.forEach(e-> {
            this.events.putIfAbsent(e.getAggregateId(), new ArrayList<>());
            this.events.get(e.getAggregateId()).add(e);
        });
    }

    @Override
    public List<Event> findAll() {
        return events.values().stream().flatMap(Collection::stream).toList();
    }

    @Override
    public List<Event> findById(long id) {
        return this.events.get(id);
    }
}

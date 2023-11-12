package io.agistep.event.storages;

import io.agistep.event.Event;

import java.util.*;

public class MapEventStorage extends OptimisticLockingSupport {

    Map<Long, List<Event>> events;

    public MapEventStorage() {
        this(new HashMap<>());
    }

    public MapEventStorage(Map<Long, List<Event>> events) {
        this.events = events;
    }

    @Override
    public void lockedSave(Event anEvent) {
        this.events.putIfAbsent(anEvent.getAggregateId(), new ArrayList<>());
        this.events.get(anEvent.getAggregateId()).add(anEvent);
    }

    @Override
    public List<Event> findByAggregate(long id) {
        return Collections.unmodifiableList(this.events.getOrDefault(id, List.of()));
    }


}

package io.agistep.event.storages;

import io.agistep.event.Event;
import io.agistep.event.Serializer;
import io.agistep.event.serialization.NoOpSerializer;

import java.util.*;

public class MapEventStorage extends OptimisticLockingSupport {

    Map<Long, List<Event>> events;
    private final Serializer serializer;

    public MapEventStorage() {
        this(new HashMap<>(), new NoOpSerializer());
    }

    public MapEventStorage(Map<Long, List<Event>> events, Serializer serializer) {
        this.events = events;
        this.serializer = serializer;
    }

    @Override
    public void lockedSave(Event anEvent) {
        this.events.putIfAbsent(anEvent.getAggregateId(), new ArrayList<>());
        if (serializer instanceof NoOpSerializer) {
            this.events.get(anEvent.getAggregateId()).add(anEvent);
        }
        this.events.get(anEvent.getAggregateId()).add(anEvent);
    }

    @Override
    public List<Event> findByAggregate(long id) {
        return Collections.unmodifiableList(this.events.getOrDefault(id, List.of()));
    }


    public Serializer getSerializer() {
        Serializer noOpSerializer = new NoOpSerializer();
        return noOpSerializer;
    }
}

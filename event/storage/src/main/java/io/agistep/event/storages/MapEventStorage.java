package io.agistep.event.storages;

import io.agistep.event.Deserializer;
import io.agistep.event.Event;
import io.agistep.event.Serializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        return this.events.getOrDefault(id, new ArrayList<>());
    }

    @Override
    public List<Serializer> supportedSerializer() {
        throw new UnsupportedOperationException("MapEventStorage not supported Serializer");
    }

    @Override
    public void addSerializer(Serializer serializer) {
        throw new UnsupportedOperationException("MapEventStorage not supported DeSerializer");
    }

    @Override
    public List<Deserializer> supportedDeSerializer(Class<?> name) {
        throw new UnsupportedOperationException("MapEventStorage not supported DeSerializer");
    }

    @Override
    public void addDeSerializer(Deserializer deserializer) {
        throw new UnsupportedOperationException("MapEventStorage not supported DeSerializer");
    }
}

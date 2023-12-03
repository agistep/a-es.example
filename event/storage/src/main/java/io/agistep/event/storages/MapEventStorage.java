package io.agistep.event.storages;

import io.agistep.event.ConvertUtil;
import io.agistep.event.Event;
import io.agistep.event.Serializer;
import io.agistep.event.serialization.NoOpSerializer;

import java.util.*;

public class MapEventStorage extends OptimisticLockingSupport {

    Map<Long, List<String>> events;
    private final Serializer serializer;

    public MapEventStorage() {
        this(new HashMap<>(), new NoOpSerializer());
    }

    public MapEventStorage(Map<Long, List<String>> events, Serializer serializer) {
        this.events = events;
        this.serializer = serializer;
    }

    @Override
    public void lockedSave(Event anEvent) {
        this.events.putIfAbsent(anEvent.getAggregateId(), new ArrayList<>());
        if (serializer instanceof NoOpSerializer) {
            String convert = ConvertUtil.simpleEventConvert(anEvent);
            this.events.get(anEvent.getAggregateId()).add(convert);
            return;
        }

        if (serializer.isSupport(anEvent.getPayload())) {
            byte[] serialize = serializer.serialize(anEvent);
            String converted = Converter.convert(serialize);
            this.events.get(anEvent.getAggregateId()).add(converted);
            return;
        }

        throw new RuntimeException("지원하지 않는 Serializer 입니다. ");
    }

    @Override
    public List<Event> findByAggregate(long id) {
        return this.events.getOrDefault(id, List.of()).stream()
                .map(ConvertUtil::simpleEventConvert)
                .toList();
    }

    public Serializer getSerializer() {
        return serializer;
    }
}

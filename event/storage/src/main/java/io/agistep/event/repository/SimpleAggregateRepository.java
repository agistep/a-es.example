package io.agistep.event.repository;

import io.agistep.event.Event;
import io.agistep.event.EventSource;
import io.agistep.event.storages.EventStorage;

import java.util.List;
import java.util.Optional;

public class SimpleAggregateRepository<AGG> implements AggregateRepository<AGG> {
    private final EventStorage storage;
    private final AggregateInitializer<AGG> initializer;


    public SimpleAggregateRepository(AggregateInitializer<AGG> initializer, EventStorage storage) {
        this.storage = storage;
        this.initializer = initializer;
        // TODO Seriazliation 선택하게끔??
    }

    @Override
    public long getNextId() {
        //TODO id generator
        throw new UnsupportedOperationException();
    }

    @Override
    public void save(AGG aggregate) {
        List<Event> events = EventSource.getHoldEvents(aggregate);

        storage.save(events);

        EventSource.clear(aggregate);
    }



    @Override
    public Optional<AGG> findById(long id) {
        List<Event> events = storage.findByAggregate(id);
        if (events==null || events.isEmpty()) {
            return Optional.empty();
        }

        AGG aggregate = initializer.initAgg();
        EventSource.replay(aggregate, events.toArray(new Event[0]));
        return Optional.of(aggregate);
    }

}

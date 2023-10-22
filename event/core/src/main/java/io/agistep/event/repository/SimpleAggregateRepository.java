package io.agistep.event.repository;

import io.agistep.event.Event;
import io.agistep.event.EventHolder;
import io.agistep.event.EventReorganizor;
import io.agistep.event.storages.EventStorage;
import io.agistep.identity.IdentityValueProvider;

import java.util.List;
import java.util.Optional;

public class SimpleAggregateRepository<AGG> implements AggregateRepository<AGG> {
    private final EventStorage storage;
    private final AggregateInitializer<AGG> initializer;


    public SimpleAggregateRepository(AggregateInitializer<AGG> initializer, EventStorage storage) {
        this.storage = storage;
        this.initializer = initializer;
    }

    @Override
    public long getNextId() {
        //TODO id generator
        return IdentityValueProvider.instance().newLong();
    }

    @Override
    public void save(AGG aggregate) {
        List<Event> events = EventHolder.instance().getEvents(aggregate);

        storage.save(events);

        EventLogger.log(events);

        EventHolder.instance().clear(aggregate);
    }



    @Override
    public Optional<AGG> findById(long id) {
        List<Event> events = storage.findById(id);
        if (events==null || events.isEmpty()) {
            return Optional.empty();
        }

        AGG aggregate = initializer.initAgg();
        EventReorganizor.reorganize(aggregate, events.toArray(new Event[0]));
        return Optional.of(aggregate);
    }

}

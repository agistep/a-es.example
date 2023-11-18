package io.agistep.event.storages;

import io.agistep.event.Event;

import java.util.List;

public interface EventStorage {

    default void save(List<Event> events) {
        events.forEach(this::save);
    }

    void save(Event anEvent);

    List<Event> findByAggregate(long id);

    default long findLatestSeqOfAggregate(long id) {
        List<Event> byAggregate = findByAggregate(id);

        return byAggregate.size() == 0 ? -1 : byAggregate.get(byAggregate.size()-1).getSeq();
    }

}

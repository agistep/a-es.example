package io.agistep.event.storages;

import io.agistep.event.Deserializer;
import io.agistep.event.Event;
import io.agistep.event.Serializer;

import java.util.List;

public interface EventStorage {

    default void save(List<Event> events) {
        events.forEach(this::save);
    }

    void save(Event anEvent);

    List<Event> findByAggregate(long id);

    /**
     *
     *
     * @param id aggregate id
     * @return if returns -1 .....
     */
    default long findLatestSeqOfAggregate(long id) {
        List<Event> byAggregate = findByAggregate(id);

        return eventNotExist(byAggregate) ? -1 : byAggregate.get(lastIndex(byAggregate)).getSeq();
    }

    private static boolean eventNotExist(List<Event> byAggregate) {
        return byAggregate.size() == 0;
    }

    private static int lastIndex(List<Event> byAggregate) {
        return byAggregate.size() - 1;
    }

    Serializer[] supportedSerializer();

    Deserializer[] supportedDeSerializer();
}

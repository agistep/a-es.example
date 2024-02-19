package io.agistep.event.storages;

import io.agistep.event.Event;
import io.agistep.event.EventMaker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static io.agistep.event.EventMaker.*;
import static org.assertj.core.api.Assertions.assertThat;

class JDBCEventStorage_JSON_Test {

    JDBCEventStorage eventStorage;

    @BeforeEach
    void setUp() {
        String url ="jdbc:tc:postgresql:9.6.8:///eventStorage?TC_INITSCRIPT=init-event-storage.sql";
        String driverName = "org.testcontainers.jdbc.ContainerDatabaseDriver";
        eventStorage = new JDBCEventStorage(driverName, url);
    }

    private record Person(String name, int age) {}

    @Test
    void saveAndFind() {

        Person john = new Person("John", 30);
        Event e = EventMaker.make(eventId(1L), aggregateId(11L), seq(1L), eventName(john.getClass().getName()), occurredAt(LocalDateTime.now()), payload(john));

        eventStorage.save(e);

        long latestSeq = eventStorage.findLatestSeqOfAggregate(e.getAggregateId());

        assertThat(latestSeq).isEqualTo(e.getSeq());

        List<Event> byAggregate = eventStorage.findByAggregate(11L);
        assertThat(byAggregate).hasSize(1);
        assertThat(byAggregate.get(0).getId()).isEqualTo(e.getId());
        assertThat(byAggregate.get(0).getSeq()).isEqualTo(e.getSeq());
        assertThat(byAggregate.get(0).getName()).isEqualTo(e.getName());
        assertThat(byAggregate.get(0).getOccurredAt()).isEqualTo(e.getOccurredAt());

        assertThat(byAggregate.get(0).getPayload()).isEqualTo(john);
    }

}
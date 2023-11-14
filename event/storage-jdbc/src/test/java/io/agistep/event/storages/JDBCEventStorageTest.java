package io.agistep.event.storages;

import io.agistep.event.Event;
import io.agistep.event.Events;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

class JDBCEventStorageTest {

    JDBCEventStorage eventStorage;

    @BeforeEach
    void setUp() {
        String url ="jdbc:tc:postgresql:9.6.8:///eventStorage?TC_INITSCRIPT=init-event-storage.sql";
        String driverName = "org.testcontainers.jdbc.ContainerDatabaseDriver";
        eventStorage = new JDBCEventStorage(driverName, url);
    }

    @Test
    void saveTest() {
        String anyPayload = "{'a': 1, 'b':2}";

        Event e = Events.builder()
                .id(1L)
                .aggregateId(11L)
                .name("foo")
                .payload(anyPayload)
                .seq(1L)
                .occurredAt(LocalDateTime.now()).build();

        eventStorage.save(e);

        List<Event> byAggregate = eventStorage.findByAggregate(11L);
        Assertions.assertThat(byAggregate).hasSize(1);
    }
}
package io.agistep.event.storages;

import io.agistep.event.Event;
import io.agistep.event.EventMaker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static io.agistep.event.EventMaker.*;
import static org.assertj.core.api.Assertions.assertThat;

class JDBCEventStorage_Protobuf_Test {

    JDBCEventStorage eventStorage;

    @BeforeEach
    void setUp() {
        String url = "jdbc:tc:postgresql:9.6.8:///eventStorage?TC_INITSCRIPT=init-event-storage.sql";
        String driverName = "org.testcontainers.jdbc.ContainerDatabaseDriver";
        eventStorage = new JDBCEventStorage(driverName, url);
    }

    @Test
    void protoBufSave() {

        ProtoPayload build = ProtoPayload.newBuilder().setBody("foo-body").build();

        Event e = EventMaker.make(eventId(12L), aggregateId(12L), seq(1L), eventName(build.getClass().getName()), occurredAt(LocalDateTime.now()), payload(build));

        eventStorage.save(e);

        List<Event> byAggregate = eventStorage.findByAggregate(12L);

        assertThat(byAggregate.get(0)).isEqualTo(e);
    }
}

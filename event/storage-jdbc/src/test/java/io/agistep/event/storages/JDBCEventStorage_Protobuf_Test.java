package io.agistep.event.storages;

import io.agistep.event.Event;
import io.agistep.event.Events;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

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

        Event e = Events.builder()
                .id(12L)
                .aggregateId(12L)
                .name("io.agistep.event.storages.ProtoPayload")
                .payload(build)
                .seq(1L)
                .occurredAt(LocalDateTime.now()).build();

        eventStorage.save(e);

        List<Event> byAggregate = eventStorage.findByAggregateAndPayloadType(12L);

        assertThat(byAggregate.get(0)).isEqualTo(e);
    }
}

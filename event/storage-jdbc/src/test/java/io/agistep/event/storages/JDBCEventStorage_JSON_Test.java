package io.agistep.event.storages;

import io.agistep.event.Event;
import io.agistep.event.Events;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JDBCEventStorage_JSON_Test {

    private static String ANY_EVENT_NAME = "io.agistep.event.storages.ProtoPayload";;

    JDBCEventStorage eventStorage;

    @BeforeEach
    void setUp() {
        String url ="jdbc:tc:postgresql:9.6.8:///eventStorage?TC_INITSCRIPT=init-event-storage.sql";
        String driverName = "org.testcontainers.jdbc.ContainerDatabaseDriver";
        eventStorage = new JDBCEventStorage(driverName, url);
    }
    @Test
    void saveAndFind() {
        String anyJsonPayload = "{"
                + "\"name\": \"John\","
                + "\"age\": 30"
                + "}";

        Event e = Events.builder()
                .id(1L)
                .aggregateId(11L)
                .name(ANY_EVENT_NAME)
                .payload(anyJsonPayload)
                .seq(1L)
                .occurredAt(LocalDateTime.now()).build();

        eventStorage.save(e);

        long latestSeq = eventStorage.findLatestSeqOfAggregate(e.getAggregateId());

        assertThat(latestSeq).isEqualTo(e.getSeq());

        List<Event> byAggregate = eventStorage.findByAggregate(11L);
        assertThat(byAggregate).hasSize(1);
        assertThat(byAggregate.get(0).getId()).isEqualTo(e.getId());
        assertThat(byAggregate.get(0).getSeq()).isEqualTo(e.getSeq());
        assertThat(byAggregate.get(0).getName()).isEqualTo(e.getName());
        assertThat(byAggregate.get(0).getOccurredAt()).isEqualTo(e.getOccurredAt());

        assertThat(byAggregate.get(0).getPayload()).isEqualTo(anyJsonPayload);
    }

}
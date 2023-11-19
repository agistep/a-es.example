package io.agistep.event.storages;

import io.agistep.event.Event;
import io.agistep.event.Events;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("ClassNamingConvention")
class JDBCEventStorage_Optimistic_Lock_Test {

    private static String ANY_EVENT_NAME = "io.agistep.event.storages.ProtoPayload";;

    JDBCEventStorage eventStorage;

    @BeforeEach
    void setUp() {
        String url ="jdbc:tc:postgresql:9.6.8:///eventStorage?TC_INITSCRIPT=init-event-storage.sql";
        String driverName = "org.testcontainers.jdbc.ContainerDatabaseDriver";
        eventStorage = new JDBCEventStorage(driverName, url);
    }

    @Test
    @DisplayName("OptimisticLockedException")
    void optimisticLockedException() {

        Event e0 = eventBySeq(0);
        Event e1 = eventBySeq(e0.getSeq());
        eventStorage.save(e0);

        assertThatThrownBy(()-> eventStorage.save(e1))
                .isInstanceOf(OptimisticLockedException.class);
    }

    @NotNull
    private static Event eventBySeq(long seq0) {
        String anyJsonPayload = "{"
                + "\"name\": \"John\","
                + "\"age\": 30"
                + "}";

        return Events.builder()
                .id(13L)
                .aggregateId(5L)
                .name(ANY_EVENT_NAME)
                .payload(anyJsonPayload)
                .seq(seq0)
                .occurredAt(LocalDateTime.now()).build();
    }
}
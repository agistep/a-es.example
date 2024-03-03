package io.agistep.event.storages;

import io.agistep.event.Event;
import io.agistep.event.EventMaker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static io.agistep.event.EventMaker.*;
import static io.agistep.event.EventMaker.payload;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("ClassNamingConvention")
class MapEventStorage_Optimistic_Locking_Test {

    MapEventStorage sut;

    TestPayload payload = TestPayload.of("Hello~~~");
    Event anEvent1 = EventMaker.make(eventId(1L), aggregateId(1L), seq(0L), eventName(payload.getClass().getName()), occurredAt(LocalDateTime.now()), payload(payload));
    Event anEvent2 = EventMaker.make(eventId(1L), aggregateId(1L), seq(1L), eventName(payload.getClass().getName()), occurredAt(LocalDateTime.now()), payload(payload));
    Event anEvent3 = EventMaker.make(eventId(1L), aggregateId(1L), seq(1L), eventName(payload.getClass().getName()), occurredAt(LocalDateTime.now()), payload(payload));

    @BeforeEach
    void setUp() {
        this.sut = new MapEventStorage();
    }

    @Test
    @DisplayName("Save And Find")
    void save() {
        sut.save(anEvent1);
        sut.save(anEvent2);

        assertThatThrownBy(() -> sut.save(anEvent3)).isInstanceOf(OptimisticLockedException.class);
    }

    private static class TestPayload {
        private final String value;

        public TestPayload(String value) {
            this.value = value;
        }

        static TestPayload of(String value) {
            return new TestPayload(value);
        }
    }

}
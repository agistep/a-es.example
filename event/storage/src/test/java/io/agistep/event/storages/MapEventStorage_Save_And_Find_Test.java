package io.agistep.event.storages;

import io.agistep.event.Event;
import io.agistep.event.EventMaker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static io.agistep.event.EventMaker.*;
import static io.agistep.event.EventMaker.payload;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("ClassNamingConvention")
class MapEventStorage_Save_And_Find_Test {

    MapEventStorage sut;

    TestPayload payload = TestPayload.of("Hello~~~");
    Event anEvent = EventMaker.make(eventId(1L), aggregateId(1L), seq(0L), eventName(payload.getClass().getName()), occurredAt(LocalDateTime.of(2023,12,12,0,0)), payload(payload));

    @BeforeEach
    void setUp() {
        this.sut = new MapEventStorage();
    }

    @Test
    @DisplayName("Save And Find")
    void saveAndFind() {
        sut.save(anEvent);
        List<Event> actual = sut.findByAggregate(anEvent.getId());

        assertThat(actual).hasSize(1);
        assertThat(actual.get(0).getId()).isEqualTo(anEvent.getId());
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
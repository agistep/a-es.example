package io.agistep.event.storages;

import io.agistep.event.Event;
import io.agistep.event.EventSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("ClassNamingConvention")
class MapEventStorage_Save_And_Find_Test {

    MapEventStorage sut;

    Event anEvent = EventSource.builder()
            .id(1L)
            .seq(0L)

            .aggregateId(1L)

            .name("TEST")
            .payload("Hello ~~~")
            .occurredAt(LocalDateTime.of(2023,12,12,0,0))
            .build();

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
        assertThat(actual.get(0)).isEqualTo(anEvent);
    }

}
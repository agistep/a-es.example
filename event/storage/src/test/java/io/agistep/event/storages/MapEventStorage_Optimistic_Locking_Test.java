package io.agistep.event.storages;

import io.agistep.event.Event;
import io.agistep.event.Events;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("ClassNamingConvention")
class MapEventStorage_Optimistic_Locking_Test {

    MapEventStorage sut;

    Event anEvent1 = Events.builder()
            .id(1L)
            .seq(0L)

            .aggregateId(1L)

            .name("TEST")
            .payload("Hello ~~~")
            .build();

    Event anEvent2 = Events.builder()
            .id(1L)
            .seq(1L)

            .aggregateId(1L)

            .name("TEST")
            .payload("Hello ~~~")
            .build();

    Event anEvent3 = Events.builder()
            .id(1L)
            .seq(1L)

            .aggregateId(1L)

            .name("TEST")
            .payload("Hello ~~~")
            .build();

    @BeforeEach
    void setUp() {
        this.sut = new MapEventStorage();
    }

    @Test
    @DisplayName("Save And Find")
    void save() {
        sut.save(anEvent1);
        sut.save(anEvent2);

        assertThatThrownBy(()-> sut.save(anEvent3)).isInstanceOf(OptimisticLockedException.class);
    }

}
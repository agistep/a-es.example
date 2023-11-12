package io.agistep.event;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("ClassNamingConvention")
class Event_Equal_Test {


    @Test
    void equals() {
        Event anEvent = Events.builder()
                .id(1L)
                .seq(0L)

                .aggregateId(1L)

                .name("TEST")
                .payload("Hello ~~~")
                .occurredAt(LocalDateTime.of(2023,12,12,0,0))
                .build();

        Event anEvent2 = Events.builder()
                .id(1L)
                .seq(0L)

                .aggregateId(1L)

                .name("TEST")
                .payload("Hello ~~~")
                .occurredAt(LocalDateTime.of(2023,12,12,0,0))
                .build();

        assertThat(anEvent).isEqualTo(anEvent2);
    }
}

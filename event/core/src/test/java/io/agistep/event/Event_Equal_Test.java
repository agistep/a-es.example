package io.agistep.event;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("ClassNamingConvention")
class Event_Equal_Test {

    private record JsonPayloadTest(String value) { }

    @Test
    void equals() {
        Event anEvent = EventSource.builder()
                .id(1L)
                .seq(0L)
                .aggregateId(1L)
                .payload(new JsonPayloadTest("Test anEvent"))
                .occurredAt(LocalDateTime.of(2023,12,12,0,0))
                .build();

        Event anEvent2 = EventSource.builder()
                .id(1L)
                .seq(0L)

                .aggregateId(1L)
                .payload(new JsonPayloadTest("Test anEvent"))
                .occurredAt(LocalDateTime.of(2023,12,12,0,0))
                .build();

        assertThat(anEvent).isEqualTo(anEvent2);
    }
}

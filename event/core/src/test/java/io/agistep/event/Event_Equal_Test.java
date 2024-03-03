package io.agistep.event;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("ClassNamingConvention")
class Event_Equal_Test {

    private record JsonPayloadTest(String value) { }

    @Test
    void equals() {

        JsonPayloadTest test_anEvent = new JsonPayloadTest("Test anEvent");
        Event anEvent = EventMaker.make(
                1L,
                1L,
                0L,
                test_anEvent.getClass().getName(),
                LocalDateTime.of(2023,12,12,0,0),
                test_anEvent
        );

        JsonPayloadTest test_anEvent2 = new JsonPayloadTest("Test anEvent");
        Event anEvent2 = EventMaker.make(
                1L,
                1L,
                0L,
                test_anEvent2.getClass().getName(),
                LocalDateTime.of(2023,12,12,0,0),
                test_anEvent
        );

        assertThat(anEvent).isEqualTo(anEvent2);
    }
}

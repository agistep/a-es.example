package io.agistep.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ThreadLocalEventListTest {


    private ThreadLocalEventList sut;

    @BeforeEach
    void setUp() {
        sut = (ThreadLocalEventList) ThreadLocalEventList.instance();
        sut.publish();
    }

    @Test
    void create() {
        assertThat(sut).isInstanceOf(ThreadLocalEventList.class);
    }

    @Test
    void occursTest() {
        long aggregateId = 1L;
        sut.occurs(Events.mock(aggregateId, -1, new FooEventPayload()));

        List<Event> actual = sut.occurredListAll();
        assertThat(actual).hasSize(1);
        Event event = actual.get(0);
        assertThat(event.getAggregateIdValue()).isEqualTo(aggregateId);
        assertThat(event.getPayload()).isInstanceOf(FooEventPayload.class);

    }

    private static class FooEventPayload {
    }

}
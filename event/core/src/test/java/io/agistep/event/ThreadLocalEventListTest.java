package io.agistep.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ThreadLocalEventListTest {


    private ThreadLocalEventList sut;

    @BeforeEach
    void setUp() {
        sut = (ThreadLocalEventList) ThreadLocalEventList.instance();
        sut.clear();
    }

    @Test
    void create() {
        assertThat(sut).isInstanceOf(ThreadLocalEventList.class);
    }

    @Test
    void occursTest() {
        long aggregateId = 1L;
        Object payload = new FooEventPayload();
        sut.occurs(new EventBuilder()
                .name(payload.getClass().getName())
                .version(ThreadLocalEventVersionMap.BEGIN_VERSION) //TODO 이전 version 를 알아야한다.
                .aggregateIdValue(aggregateId)
                .payload(payload)
                .occurredAt(LocalDateTime.now())
                .build());

        List<Event> actual = sut.occurredListAll();
        assertThat(actual).hasSize(1);
        Event event = actual.get(0);
        assertThat(event.getAggregateIdValue()).isEqualTo(aggregateId);
        assertThat(event.getPayload()).isInstanceOf(FooEventPayload.class);

    }

    private static class FooEventPayload {
    }

}
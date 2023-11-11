package io.agistep.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static io.agistep.event.test.EventFixtureBuilder.anEventWith;
import static org.assertj.core.api.Assertions.assertThat;

class ThreadLocalEventHolderTest {


    private ThreadLocalEventHolder sut;

    @BeforeEach
    void setUp() {
        sut = (ThreadLocalEventHolder) ThreadLocalEventHolder.instance();
        sut.clearAll();
    }

    @Test
    void create() {
        assertThat(sut).isInstanceOf(ThreadLocalEventHolder.class);
    }

    @Test
    void occursTest() {
        long aggregateId = 1L;
        Object payload = new FooEventPayload();
        sut.hold(Events.builder()
                        .id(1L)
                .name(payload.getClass().getName())
                .version(Events.INITIAL_VERSION)
                .aggregateId(aggregateId)
                .payload(payload)
                .occurredAt(LocalDateTime.now())
                .build());

        List<Event> actual = sut.getEventAll();
        assertThat(actual).hasSize(1);
        Event event = actual.get(0);
        assertThat(event.getAggregateId()).isEqualTo(aggregateId);
        assertThat(event.getPayload()).isInstanceOf(FooEventPayload.class);

    }


    @Test
    void clear() {
        Foo aggregate = new Foo();
        Events.reorganize(aggregate, anEventWith(new FooCreated()));

        sut.clear(aggregate);

        List<Event> events = sut.getEvents(aggregate);
        assertThat(events.stream().noneMatch(a->aggregate.getId()==a.getId())).isTrue();
    }

    private static class FooEventPayload {
    }

}
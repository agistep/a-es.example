package io.agistep.event.test;

import io.agistep.event.Event;
import io.agistep.event.Events;
import io.agistep.foo.Foo;
import io.agistep.foo.FooCreated;
import io.agistep.foo.FooDone;
import io.agistep.foo.FooReOpened;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static io.agistep.event.Events.INITIAL_SEQ;
import static io.agistep.event.test.EventFixtureBuilder.eventsWith;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EventFixtureBuilderTest {

    public static final FooCreated FIRST_PAYLOAD = new FooCreated();
    public static final FooDone SECOND_PAYLOAD = new FooDone();
    public static final FooReOpened THIRD_PAYLOAD = new FooReOpened();
    public static final Object NULL_PAYLOAD = null;


    @Test
    @DisplayName("Exception: payload must be not null")
    void forTestWith0() {
        assertThatThrownBy(()-> eventsWith(NULL_PAYLOAD));
    }

    @Test
    @DisplayName("Exception: payload must be not null")
    void forTestWith00() {
        assertThatThrownBy(()->
                eventsWith(aggregate(1L), FIRST_PAYLOAD)
                .next(NULL_PAYLOAD));
    }

    @Test
    void forTestWith1() {

        Event[] actual = eventsWith(FIRST_PAYLOAD)
                .next(SECOND_PAYLOAD)
                .build();

        //assertThat(actual[0].getId()).isEqualTo(???)
        assertThat(actual[0].getAggregateId()).isEqualTo(actual[1].getAggregateId());

        assertThat(actual[0].getSeq()).isEqualTo(INITIAL_SEQ);
        assertThat(actual[0].getName()).isEqualTo(FIRST_PAYLOAD.getClass().getName());
        assertThat(actual[0].getPayload()).isEqualTo(FIRST_PAYLOAD);
        assertThat(actual[0].getOccurredAt()).isEqualToIgnoringNanos(LocalDateTime.now());

        assertThat(actual[1].getSeq()).isEqualTo(actual[0].getSeq()+1);
        assertThat(actual[1].getName()).isEqualTo(SECOND_PAYLOAD.getClass().getName());
        assertThat(actual[1].getPayload()).isEqualTo(SECOND_PAYLOAD);
        assertThat(actual[1].getOccurredAt()).isEqualToIgnoringNanos(LocalDateTime.now());
    }

    @Test
    void forTestWith2() {

        Foo aggregate = new Foo();
        Events.reorganize(aggregate, eventsWith(FIRST_PAYLOAD)
                .next(SECOND_PAYLOAD)
                .build());
        Events.apply(aggregate, THIRD_PAYLOAD);

        Event[] expected = eventsWith(aggregate.getId(), THIRD_PAYLOAD).build();

        assertThat(expected[0].getAggregateId()).isEqualTo(aggregate.getId());

        assertThat(expected[0].getSeq()).isEqualTo(2);
        assertThat(expected[0].getName()).isEqualTo(THIRD_PAYLOAD.getClass().getName());
        assertThat(expected[0].getPayload()).isEqualTo(THIRD_PAYLOAD);
        assertThat(expected[0].getOccurredAt()).isEqualToIgnoringNanos(LocalDateTime.now());

    }

    private long aggregate(long id) {
        return id;
    }
}
package io.agistep.event.test;

import io.agistep.event.Event;
import io.agistep.event.Events;
import io.agistep.foo.Foo;
import io.agistep.foo.FooCreated;
import io.agistep.foo.FooDone;
import io.agistep.foo.FooReOpened;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static io.agistep.event.test.EventFixtureBuilder.eventsWith;
import static io.agistep.event.test.EventPredicates.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TestEventLoggerTest {
    public static final FooCreated FIRST_PAYLOAD = new FooCreated();
    public static final FooDone SECOND_PAYLOAD = new FooDone();
    public static final FooReOpened THIRD_PAYLOAD = new FooReOpened();

    TestEventLogger sut;

    @BeforeEach
    void setUp() {
        sut = new TestEventLogger(); // TODO 상태를 가지고 있다..
        Events.setListener(sut);
    }

    @AfterEach
    void tearDown() {
        Events.setListener(null); // TODO  불필요한 ... 현재 Listener 는 static 으로 유지 된다.
    }

    @Test
    @DisplayName("Get Event by index")
    void get0() {
        assertThat(sut.size()).isEqualTo(0);
        assertThatThrownBy(()->sut.get(0)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(()->sut.get(1000)).isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    void get1() {
        Foo aggregate = new Foo();
        FooCreated created = FIRST_PAYLOAD;
        FooDone done = SECOND_PAYLOAD;

        Events.apply(aggregate, created);
        Events.apply(aggregate, done);

        assertThat(sut.size()).isEqualTo(2);

        Event[] expected = eventsWith(created).next(done).build();

        Event actual1 = sut.get(0);
        assertThat(actual1).matches(sameVersion(expected[0]));
        assertThat(actual1).matches(sameName(expected[0]));
        assertThat(actual1).matches(samePayload(expected[0]));

        Event actual2 = sut.get(1);
        assertThat(actual2).matches(sameVersion(expected[1]));
        assertThat(actual2).matches(sameName(expected[1]));
        assertThat(actual2).matches(samePayload(expected[1]));

    }

    @Test
    void forTestWith2() {

        Event[] events = EventFixtureBuilder.eventsWith(FIRST_PAYLOAD)
                .next(SECOND_PAYLOAD)
                .build();
        Foo aggregate = new Foo();
        Events.reorganize(aggregate, events);

        Events.apply(aggregate, THIRD_PAYLOAD);

        Event[] expected = EventFixtureBuilder.eventsWith(aggregate.getId(), THIRD_PAYLOAD).build();

        assertThat(expected[0].getAggregateId()).isEqualTo(aggregate.getId());

        assertThat(expected[0].getSeq()).isEqualTo(2);
        assertThat(expected[0].getName()).isEqualTo(THIRD_PAYLOAD.getClass().getName());
        assertThat(expected[0].getPayload()).isEqualTo(THIRD_PAYLOAD);
        assertThat(expected[0].getOccurredAt()).isEqualToIgnoringNanos(LocalDateTime.now());

    }



}
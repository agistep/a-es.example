package io.agistep.event.test;

import io.agistep.aggregator.IdUtils;
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

import java.util.function.Consumer;
import java.util.function.Supplier;

import static io.agistep.event.test.EventFixtureBuilder.eventsWith;
import static io.agistep.event.test.EventMatchConditions.*;
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
        assertThatThrownBy(() -> sut.get(0)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> sut.get(1000)).isInstanceOf(IndexOutOfBoundsException.class);
    }

    static class Pair {
        final Object aggregate;
        final long latestSeq;

        Pair(Object aggregate, long latestSeq) {
            this.aggregate = aggregate;
            this.latestSeq = latestSeq;
        }
    }

    @Test
    void case1() {
        Event[] recently = new Event[0];
        Object[] expected = {FIRST_PAYLOAD, SECOND_PAYLOAD};

        testEventSourcing(
                recently,

                (aggregate) -> {
                    Events.apply(aggregate, FIRST_PAYLOAD);
                    Events.apply(aggregate, SECOND_PAYLOAD);},

                expected);
    }
    @Test
    void case2() {
        Event[] recently = eventsWith(FIRST_PAYLOAD).next(SECOND_PAYLOAD).build();
        Object[] expected = new Object[]{THIRD_PAYLOAD};

        testEventSourcing(
                recently,

                (aggregate1) -> {
                    Events.apply(aggregate1, THIRD_PAYLOAD);
                },

                expected);

    }

    private void testEventSourcing(Event[] recently, Consumer<Object> aggregateProcessor, Object[] expected) {
        Pair pair = getPair(recently, Foo::new);
        aggregateProcessor.accept(pair.aggregate);
        abc(pair.aggregate, pair.latestSeq, expected); // created 와 done 이 잘 발생했는가?
    }

    private static Pair getPair(Event[] recently, Supplier<Object> initAggregate) {
        Object aggregate = initAggregate.get();
        Events.reorganize(aggregate, recently);
        final long latestSeq = getLatestSeq(aggregate);

        return new Pair(aggregate, latestSeq);
    }

    private static long getLatestSeq(Object aggregate) {
        long seq;
        try {
            seq = Events.getLatestSeqOf(aggregate);
        } catch (Exception e) {
            seq = -1;
        }
        return seq;
    }


    private void abc(Object aggregate, long seq, Object... expectedPayload) {
        final long aggregateId = IdUtils.idOf(aggregate);

        Event[] expected = getExpected(aggregateId, seq, expectedPayload);
        Event[] actual = getActual();

        assertThat(actual.length).isEqualTo(expected.length);

        for (int i = 0; i < expected.length; i++) {
            //assertThat(actual[i]).is(idCondition(expected[i]));
            assertThat(actual[i]).is(aggregateIdCondition(expected[i]));
            assertThat(actual[i]).is(seqCondition(expected[i]));
            assertThat(actual[i]).is(nameCondition(expected[i]));
            assertThat(actual[i]).is(payloadCondition(expected[i]));
            assertThat(actual[i]).is(occurredAtCondition(expected[i]));
        }
    }

    private Event[] getActual() {
        return this.sut.getAll();
    }

    private static Event[] getExpected(long aggregateId, long seq, Object[] expectedPayload) {
        if (expectedPayload == null || expectedPayload.length == 0) {
            return new Event[0];
        }
        EventFixtureBuilder eventsWith = eventsWith(aggregateId, expectedPayload[0]);

        for (int i = 1; i < expectedPayload.length; ++i) {
            eventsWith.next(expectedPayload[i]);
        }
        return eventsWith.build(seq);
    }


}
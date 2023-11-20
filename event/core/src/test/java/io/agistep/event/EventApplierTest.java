package io.agistep.event;

import io.agistep.foo.Foo;
import io.agistep.foo.FooCreated;
import io.agistep.foo.FooDone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EventApplierTest {

    @BeforeEach
    void setUp() {
        EventSource.clearAll();
    }

    @Test
    void apply() {
        Foo aggregate = new Foo();

        assertThat(EventSource.getHoldEvents(aggregate)).hasSize(0);

        EventSource.apply(aggregate, new FooCreated());

        assertThat(aggregate.isDone()).isFalse();
        assertThat(EventSource.getHoldEvents(aggregate)).hasSize(1);
        assertThat(EventSource.getLatestSeqOf(aggregate)).isEqualTo(EventSource.INITIAL_SEQ);

        EventSource.apply(aggregate, new FooDone());

        assertThat(aggregate.isDone()).isTrue();
        assertThat(EventSource.getHoldEvents(aggregate)).hasSize(2);
        assertThat(EventSource.getLatestSeqOf(aggregate)).isEqualTo(EventSource.INITIAL_SEQ +1);

    }
}
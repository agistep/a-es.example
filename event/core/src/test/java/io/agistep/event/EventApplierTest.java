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
        Events.clearAll();
    }

    @Test
    void apply() {
        Foo aggregate = new Foo();

        assertThat(Events.getHoldEvents(aggregate)).hasSize(0);

        Events.apply(aggregate, new FooCreated());

        assertThat(aggregate.isDone()).isFalse();
        assertThat(Events.getHoldEvents(aggregate)).hasSize(1);
        assertThat(Events.getLatestSeqOf(aggregate)).isEqualTo(Events.INITIAL_SEQ);

        Events.apply(aggregate, new FooDone());

        assertThat(aggregate.isDone()).isTrue();
        assertThat(Events.getHoldEvents(aggregate)).hasSize(2);
        assertThat(Events.getLatestSeqOf(aggregate)).isEqualTo(Events.INITIAL_SEQ +1);

    }
}
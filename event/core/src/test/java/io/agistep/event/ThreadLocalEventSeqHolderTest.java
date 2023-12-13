package io.agistep.event;

import io.agistep.foo.Foo;
import io.agistep.foo.FooCreated;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.agistep.event.test.EventFixtureBuilder.anEventWith;
import static org.assertj.core.api.Assertions.assertThat;

class ThreadLocalEventSeqHolderTest {

    @BeforeEach
    void setUp() {
        EventSource.clearAll();
    }

    @Test
    void name() {
        assertThat(EventSource.getLatestSeqOf(1L)).isEqualTo(-1);

        ThreadLocalEventSeqHolder.instance().setSeq(1L,0);
        assertThat(EventSource.getLatestSeqOf(1L)).isEqualTo(0);

        ThreadLocalEventSeqHolder.instance().setSeq(1L,1);
        assertThat(EventSource.getLatestSeqOf(1L)).isEqualTo(1);

        EventSource.clearAll();

        assertThat(EventSource.getLatestSeqOf(1L)).isEqualTo(-1);

    }

    @Test
    @DisplayName("remove the sequence of an aggregateId when aggregate is cleared")
    void clear() {
        Foo aggregate1 = new Foo();
        EventSource.replay(aggregate1, anEventWith(new FooCreated()));

        Foo aggregate2 = new Foo();
        EventSource.replay(aggregate2, anEventWith(new FooCreated()));

        var sut = ThreadLocalEventSeqHolder.instance();
        sut.clear(aggregate1);

        assertThat(sut.getSeq(aggregate1.getId())).isEqualTo(-1);
        assertThat(sut.getSeq(aggregate2.getId())).isEqualTo(0);
    }
}

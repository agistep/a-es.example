package io.agistep.event;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EventApplierTest {

    @Test
    void apply() {
        Foo aggregate = new Foo();

        assertThat(EventList.instance().occurredListBy(aggregate)).hasSize(0);

        EventApplier.instance().apply(aggregate, new FooCreated());
        assertThat(EventList.instance().occurredListBy(aggregate)).hasSize(1);

        EventApplier.instance().apply(aggregate, new FooDone());
        assertThat(EventList.instance().occurredListBy(aggregate)).hasSize(2);

    }
}
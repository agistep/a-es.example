package io.agistep.event;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

class EventApplierTest {

    @Test
    void apply() {
        Foo aggregate = new Foo();

        assertThat(EventList.instance().occurredListBy(aggregate)).hasSize(0);

        EventApplier.instance().apply(aggregate, new FooCreated(), new HashMap<>());
        assertThat(EventList.instance().occurredListBy(aggregate)).hasSize(1);

        EventApplier.instance().apply(aggregate, new FooDone(), new HashMap<>());
        assertThat(EventList.instance().occurredListBy(aggregate)).hasSize(2);

    }
}
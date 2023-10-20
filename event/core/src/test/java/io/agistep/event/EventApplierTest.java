package io.agistep.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EventApplierTest {

    @BeforeEach
    void setUp() {
        EventHolder.instance().clearAll();
    }

    @Test
    void apply() {
        Foo aggregate = new Foo(()->1L);

        assertThat(EventHolder.instance().getEvents(aggregate)).hasSize(0);

        EventApplier.instance().apply(aggregate, new FooCreated());
        assertThat(EventHolder.instance().getEvents(aggregate)).hasSize(1);

        EventApplier.instance().apply(aggregate, new FooDone());
        assertThat(EventHolder.instance().getEvents(aggregate)).hasSize(2);

    }
}
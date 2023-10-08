package io.agistep.event;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("ClassNamingConvention")
class Ordering_EventApplierTest {

    @Test
    void apply() {
        ThreadLocalOrderMap.instance().clear();

        Foo aggregate = new Foo();

        final EventList eventList = EventList.instance();

        EventApplier.instance().apply(aggregate, new FooCreated());
        assertThat(eventList.getLatestOrderOf(aggregate)).isEqualTo(0);

        EventApplier.instance().apply(aggregate, new FooDone());
        assertThat(eventList.getLatestOrderOf(aggregate)).isEqualTo(1);

        EventApplier.instance().apply(aggregate, new FooDone());
        assertThat(eventList.getLatestOrderOf(aggregate)).isEqualTo(2);

    }
}
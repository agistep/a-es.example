package io.agistep.event;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("ClassNamingConvention")
class Ordering_EventApplierTest {

    @Test
    void apply() {
        Foo aggregate = new Foo();

        final EventList eventList = EventList.instance();

        HashMap<Long, Long> aggregateOrderMap = new HashMap<>();

        EventApplier.instance().apply(aggregate, new FooCreated(), aggregateOrderMap);
        assertThat(eventList.getLatestOrderOf(aggregate)).isEqualTo(1);

        EventApplier.instance().apply(aggregate, new FooDone(), aggregateOrderMap);
        assertThat(eventList.getLatestOrderOf(aggregate)).isEqualTo(2);

        EventApplier.instance().apply(aggregate, new FooDone(), aggregateOrderMap);
        assertThat(eventList.getLatestOrderOf(aggregate)).isEqualTo(3);

    }
}
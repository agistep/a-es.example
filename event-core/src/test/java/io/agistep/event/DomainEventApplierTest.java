package io.agistep.event;

import io.agistep.identity.Identity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DomainEventApplierTest {

    static class Foo {
        Identity<Long> id;

        void onCreated(Event anEvent) {
            this.id = anEvent::getAggregateIdValue;
        }
    }

    static class FooCreated {

    }
    static class FooDone {

    }

    @Test
    void apply() {
        Foo aggregate = new Foo();

        assertThat(EventList.instance().occurredListBy(aggregate)).hasSize(0);

        DomainEventApplier.instance().apply(aggregate, new FooCreated());
        assertThat(EventList.instance().occurredListBy(aggregate)).hasSize(1);

        DomainEventApplier.instance().apply(aggregate, new FooDone());
        assertThat(EventList.instance().occurredListBy(aggregate)).hasSize(2);

    }
}
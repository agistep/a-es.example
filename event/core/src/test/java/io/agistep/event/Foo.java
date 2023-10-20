package io.agistep.event;

import io.agistep.identity.Identity;

class Foo {
    Identity<Long> id;

    public Foo(Identity<Long> id) {
        this.id = id;
    }

    @EventHandler(payload=FooCreated.class)
    void onCreated(Event anEvent) {
        this.id = anEvent::getAggregateId;
    }

    @EventHandler(payload=FooDone.class)
    void onDone(Event anEvent) {

    }

}

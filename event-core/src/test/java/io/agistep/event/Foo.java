package io.agistep.event;

import io.agistep.identity.Identity;

class Foo {

    public Foo() {
    }

    Foo(String text) {
        FooCreated created = new FooCreated();
        EventApplier.instance().apply(this, created);
    }


    Identity<Long> id;

    @EventHandler(payload=FooCreated.class)
    void onCreated(Event anEvent) {
        this.id = anEvent::getAggregateIdValue;
    }

    @EventHandler(payload=FooDone.class)
    void onDone(Event anEvent) {

    }

}

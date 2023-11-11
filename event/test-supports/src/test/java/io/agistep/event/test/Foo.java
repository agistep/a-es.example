package io.agistep.event.test;


import io.agistep.event.Event;
import io.agistep.event.EventHandler;

class Foo {

    Long id;

    boolean done = false;

    public Foo() {}

    @EventHandler(payload= FooCreated.class)
    void onCreated(Event anEvent) {
        this.id = (Long) anEvent.getAggregateId();
    }

    @EventHandler(payload=FooDone.class)
    void onDone(Event anEvent) {
        this.done = true;
    }

    @EventHandler(payload= FooReOpened.class)
    void onReOpened(Event anEvent) {
        this.done = false;
    }

    Long getId() {
        return id;
    }

    boolean isDone() {
        return done;
    }

}

package io.agistep.foo;


import io.agistep.event.Event;
import io.agistep.event.EventHandler;

public class Foo {

    long id;

    boolean done = false;

    public Foo() {}

    @EventHandler(payload= FooCreated.class)
    void onCreated(Event anEvent) {
        this.id = anEvent.getAggregateId();
    }

    @EventHandler(payload=FooDone.class)
    void onDone(Event anEvent) {
        this.done = true;
    }

    @EventHandler(payload= FooReOpened.class)
    void onReOpened(Event anEvent) {
        this.done = false;
    }

    public long getId() {
        return id;
    }

    public boolean isDone() {
        return done;
    }

}

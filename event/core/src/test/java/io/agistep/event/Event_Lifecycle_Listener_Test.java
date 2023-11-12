package io.agistep.event;


import io.agistep.foo.Foo;
import io.agistep.foo.FooCreated;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.InOrder;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

@SuppressWarnings("ClassNamingConvention")
class Event_Lifecycle_Listener_Test {

    static final FooCreated CREATED = new FooCreated();

    @Test
    void name() {
        HoldListener holdListener = mock(HoldListener.class);
        ReorganizeListener reorganizeListener = mock(ReorganizeListener.class);
        Events.setListener(holdListener);
        Events.setListener(reorganizeListener);

        Foo aggregate = new Foo();
        Events.apply(aggregate, CREATED);

        InOrder inOrder = inOrder(holdListener, reorganizeListener);

        inOrder.verify(reorganizeListener).beforeReorganize(eq(aggregate), argThat(classNameEqualTo(CREATED)));
        inOrder.verify(reorganizeListener).afterReorganize(eq(aggregate), argThat(classNameEqualTo(CREATED)));
        inOrder.verify(holdListener).beforeHold(argThat(classNameEqualTo(CREATED)));
        inOrder.verify(holdListener).afterHold(argThat(classNameEqualTo(CREATED)));

    }

    private static  ArgumentMatcher<Event> classNameEqualTo(Object payload) {
        return arg -> payload.getClass().getName().equals(arg.getName());
    }
}

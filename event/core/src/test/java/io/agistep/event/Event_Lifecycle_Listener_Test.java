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
        ReplayListener replayListener = mock(ReplayListener.class);
        EventSource.setListener(holdListener);
        EventSource.setListener(replayListener);

        Foo aggregate = new Foo();
        EventSource.apply(aggregate, CREATED);

        InOrder inOrder = inOrder(holdListener, replayListener);

        inOrder.verify(replayListener).beforeReplay(eq(aggregate), argThat(classNameEqualTo(CREATED)));
        inOrder.verify(replayListener).afterReplay(eq(aggregate), argThat(classNameEqualTo(CREATED)));
        inOrder.verify(holdListener).beforeHold(argThat(classNameEqualTo(CREATED)));
        inOrder.verify(holdListener).afterHold(argThat(classNameEqualTo(CREATED)));

    }

    private static  ArgumentMatcher<Event> classNameEqualTo(Object payload) {
        return arg -> payload.getClass().getName().equals(arg.getName());
    }
}

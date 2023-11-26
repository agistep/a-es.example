package io.agistep.utils;

import io.agistep.event.Event;
import io.agistep.foo.FooCreated;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class MethodHelperTest {

    @Test
    @DisplayName("invoke method with parameter of Event")
    void invoke0() throws Exception {
        FooAggregate aggregate = spy(new FooAggregate());
        Event event = mock(Event.class);
        Method method = FooAggregate.class.getMethod("methodWithOneEventParam", Event.class);

        MethodHelper.invoke(aggregate, event, method);

        ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
        verify(aggregate).methodWithOneEventParam(eventCaptor.capture());

        assertSame(event, eventCaptor.getValue());
    }

    @Test
    @DisplayName("invoke method with parameters of payload and aggregate id")
    void invoke1() throws Exception {
        FooAggregate aggregate = spy(new FooAggregate());

        Event event = mock(Event.class);
        when(event.getAggregateId()).thenReturn(1L);
        FooCreated payload = new FooCreated();
        when(event.getPayload()).thenReturn(payload);

        Method method = FooAggregate.class.getMethod("methodWithTwoParams", Object.class, Long.class);

        MethodHelper.invoke(aggregate, event, method);

        verify(aggregate).methodWithTwoParams(payload, 1L);
    }

    @Test
    @DisplayName("invoke method with invalid parameters")
    void invoke2() throws NoSuchMethodException {
        Object aggregate = new FooAggregate();
        Event event = mock(Event.class);
        Method method = FooAggregate.class.getMethod("methodWithInvalidParams", String.class, String.class);

        assertThrows(IllegalArgumentException.class, () -> {
            MethodHelper.invoke(aggregate, event, method);
        });
    }

    static class FooAggregate {
        public void methodWithOneEventParam(Event event) {}

        public void methodWithTwoParams(Object payload, Long aggregateId) {}

        public void methodWithInvalidParams(String param1, String param2) {}
    }
}

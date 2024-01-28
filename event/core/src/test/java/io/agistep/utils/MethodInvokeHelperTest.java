package io.agistep.utils;

import io.agistep.aggregator.Aggregate;
import io.agistep.event.Event;
import io.agistep.foo.FooCreated;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class MethodInvokeHelperTest {

    @Test
    @DisplayName("Exception: invoke method with no parameters")
    void invoke0() throws NoSuchMethodException {
        Aggregate aggregate = new FooAggregate();
        Event event = mock(Event.class);
        Method method = FooAggregate.class.getMethod("methodWithNoParams");

        assertThrows(IllegalArgumentException.class, () -> {
            MethodInvokeHelper.invoke(aggregate, event, method);
        });
    }

    @Test
    @DisplayName("Exception: invoke method with invalid parameters")
    void invoke1() throws NoSuchMethodException {
        Aggregate aggregate = new FooAggregate();
        Event event = mock(Event.class);
        Method method = FooAggregate.class.getMethod("methodWithInvalidParams", String.class, String.class);

        assertThrows(IllegalArgumentException.class, () -> {
            MethodInvokeHelper.invoke(aggregate, event, method);
        });
    }

    @Test
    @DisplayName("invoke method with parameter of Event")
    void invoke2() throws Exception {
        // given
        FooAggregate aggregate = spy(new FooAggregate());
        Event event = mock(Event.class);
        Method method = FooAggregate.class.getMethod("methodWithOneEventParam", Event.class);

        // when
        MethodInvokeHelper.invoke(aggregate, event, method);

        // then
        ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
        verify(aggregate).methodWithOneEventParam(eventCaptor.capture());

        assertSame(event, eventCaptor.getValue());
    }

    @Test
    @DisplayName("invoke method with parameter of payload")
    void invoke3() throws Exception {
        // given
        FooAggregate aggregate = spy(new FooAggregate());

        Event event = mock(Event.class);
        FooCreated payload = new FooCreated();
        when(event.getPayload()).thenReturn(payload);

        Method method = FooAggregate.class.getMethod("methodWithOnePayloadParam", Object.class);

        // when
        MethodInvokeHelper.invoke(aggregate, event, method);

        // then
        ArgumentCaptor<FooCreated> payloadCaptor = ArgumentCaptor.forClass(FooCreated.class);
        verify(aggregate).methodWithOnePayloadParam(payloadCaptor.capture());

        assertSame(payload, payloadCaptor.getValue());
    }

    @Test
    @DisplayName("invoke method with parameters of payload and aggregate id")
    void invoke4() throws Exception {
        // given
        FooAggregate aggregate = spy(new FooAggregate());

        Event event = mock(Event.class);
        when(event.getAggregateId()).thenReturn(1L);
        FooCreated payload = new FooCreated();
        when(event.getPayload()).thenReturn(payload);

        Method method = FooAggregate.class.getMethod("methodWithTwoParams", Object.class, Long.class);

        // when
        MethodInvokeHelper.invoke(aggregate, event, method);

        // then
        verify(aggregate).methodWithTwoParams(payload, 1L);
    }

    static class FooAggregate implements Aggregate {
        public void methodWithNoParams() {}

        public void methodWithOneEventParam(Event event) {}

        public void methodWithOnePayloadParam(Object payload) {}

        public void methodWithTwoParams(Object payload, Long aggregateId) {}

        public void methodWithInvalidParams(String param1, String param2) {}

        @Override
        public long getId() {
            return 0;
        }
    }
}

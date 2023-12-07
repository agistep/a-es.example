package io.agistep.event;

import io.agistep.foo.Foo;
import io.agistep.foo.FooCreated;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.NoSuchElementException;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@SuppressWarnings("ClassNamingConvention")
class HandlerAdapter_Exception_Test {
    private static final List<Pair<EventHandler, Method>> HANDLER_METHODS_IS_NOT_EXIST = emptyList();

    @Test
    @DisplayName("No Handler for %s Present in %s.")
    void name() {
        Foo foo = new Foo();
        HandlerAdapter sut = new HandlerAdapter(new Foo(), HANDLER_METHODS_IS_NOT_EXIST);

        Event event = mock();
        String eventName = FooCreated.class.getName();
        given(event.getName()).willReturn(eventName);

        assertThatThrownBy(()->sut.handle(foo, event))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("No Handler for %s Present in %s.", eventName, foo.getClass().getName());
    }
}
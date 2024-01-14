package io.agistep.event;

import io.agistep.foo.Foo;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("ClassNamingConvention")
class EventHandlerMethodAdapter_Exception_Test {
    private static final List<Pair<EventHandler, Method>> HANDLER_METHODS_IS_NOT_EXIST = emptyList();

    @Test
    @DisplayName("init() throws IllegalArgumentException if method is null")
    void name() throws NoSuchMethodException {
        Foo foo = new Foo();

        assertThatThrownBy(() -> EventHandlerMethodAdapter.init(foo.getClass().getMethod("getId")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("EventHandler annotation is not present on method: getId");
    }
}

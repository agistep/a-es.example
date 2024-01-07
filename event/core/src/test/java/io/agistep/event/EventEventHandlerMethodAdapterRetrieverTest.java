package io.agistep.event;

import io.agistep.foo.FooReOpened;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EventEventHandlerMethodAdapterRetrieverTest {

    EventHandlerAdapterRetrieverImpl sut;

    @BeforeEach
    void setUp() {
        System.setProperty("basePackage", "io.agistep");

        List<EventHandlerMethodAdapter> eventHandlerMethodAdapters = List.of(
                mockHandlerAdapter(FooReOpened.class.getName())
        );

        sut = new EventHandlerAdapterRetrieverImpl(eventHandlerMethodAdapters);
    }

    private EventHandlerMethodAdapter mockHandlerAdapter(String payloadName) {
        EventHandlerMethodAdapter adapter = mock(EventHandlerMethodAdapter.class);
        when(adapter.getPayloadName()).thenReturn(payloadName);
        return adapter;
    }

    @Test
    void retrieveNullIfNoEventHandler() {
        EventHandlerMethodAdapter eventHandlerMethodAdapter = sut.retrieve("NotExistClassName");
        assertThat(eventHandlerMethodAdapter).isEqualTo(null);
    }

    @Test
    void initAllEventHandlers() {
        EventHandlerMethodAdapter eventHandlerMethodAdapter = sut.retrieve(FooReOpened.class.getName());
        assertThat(eventHandlerMethodAdapter).isNotNull();
        assertThat(eventHandlerMethodAdapter.getPayloadName()).isEqualTo(FooReOpened.class.getName());
    }

}

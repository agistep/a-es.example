package io.agistep.event;

import io.agistep.foo.Foo;
import io.agistep.foo.FooDone;
import io.agistep.foo.FooReOpened;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EventHandlerLoaderTest {

    private EventHandlerLoader sut = new EventHandlerLoader();

    @Test
    void retrieveNullIfNoEventHandler() {
        HandlerAdapter handlerAdapter = sut.retrieveHandler("NotExistClassName");
        assertThat(handlerAdapter).isEqualTo(null);
    }

    @Test
    void initAllEventHandlers() {
        HandlerAdapter handlerAdapter = sut.retrieveHandler(FooReOpened.class.getName());
        assertThat(handlerAdapter.getAggregateName()).isEqualTo(Foo.class.getName());
    }

    @Test
    void initAllEventHandlers2() {
        HandlerAdapter handlerAdapter = sut.retrieveHandler(FooDone.class.getName());
        assertThat(handlerAdapter.getAggregateName()).isEqualTo(Foo.class.getName());
    }

}

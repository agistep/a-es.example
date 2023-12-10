package io.agistep.event;

import io.agistep.foo.FooCreated;
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
        HandlerAdapter handlerAdapter = sut.retrieveHandler(FooCreated.class.getName());
        assertThat(handlerAdapter.getAggregateName()).isEqualTo(FooTestAgg.class.getName());
    }

    static class FooTestAgg {
        @EventHandler(payload = FooCreated.class)
        void handle(FooCreated event) {
            // dummy method
        }
    }

}

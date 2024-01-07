package io.agistep.event;

import io.agistep.foo.Foo;
import io.agistep.utils.AnnotationHelper;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class EventHandlerLoaderTest extends EventApplySupport {

    private EventHandlerMethodAdapterLoader sut;

    @BeforeEach
    void setUp() {
        sut = new EventHandlerMethodAdapterLoader(new EventHandlerMethodScannerImpl(), new EventHandlerAdapterInitializerImpl());
    }

    @Test
    void aaa() throws NoSuchMethodException {
        EventHandlerMethodScanner eventHandlerScanner = new EventHandlerMethodScannerImpl();
        EventHandlerAdapterInitializer eventHandlerAdapterInitializer = new EventHandlerAdapterInitializerImpl();

        sut = new EventHandlerMethodAdapterLoader(eventHandlerScanner, eventHandlerAdapterInitializer);

        List<EventHandlerMethodAdapter> actual = sut.load("io.agistep.foo");
        var m = Foo.class.getDeclaredMethod("onReOpened", Event.class);

        assertTrue(actual.stream().anyMatch(a -> a.getAggregateName().equals(Foo.class.getName())
                && a.getPair(0).equals(Pair.of(AnnotationHelper.getAnnotation(m, EventHandler.class), m))
                )
        );

    }

}

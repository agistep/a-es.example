package io.agistep.event;

import io.agistep.utils.AnnotationHelper;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

class EventHandlerTest {


    @Test
    void eventHandler() {
        List<EventHandler> actual = AnnotationHelper
                .getMethodsListWithAnnotation(Foo.class, EventHandler.class)
                .stream().map(m -> m.getAnnotation(EventHandler.class)).collect(toList());

        assertThat(actual).contains(
                eventHandler(FooCreated.class),
                eventHandler(FooDone.class),
                eventHandler(FooReOpened.class));
        assertThat(actual).hasSize(3);
    }

    private static EventHandler eventHandler(Class<?> aClass) {
        return new EventHandler() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return EventHandler.class;
            }

            @Override
            public Class<?> payload() {
                return aClass;
            }
        };
    }


}

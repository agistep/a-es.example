package io.agistep.event;

import io.agistep.utils.AnnotationHelper;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class EventHandlerTest {


    @Test
    void name() {
        List<Method> actual = AnnotationHelper.getMethodsListWithAnnotation(Foo.class, EventHandler.class);
        EventHandler eventHandler = actual.get(0).getAnnotation(EventHandler.class);

        assertThat(eventHandler.payload()).isEqualTo(FooCreated.class);
        assertThat(actual).hasSize(2);
    }


}

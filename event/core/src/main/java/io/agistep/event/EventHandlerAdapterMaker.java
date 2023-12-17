package io.agistep.event;

import java.lang.reflect.Method;

public interface EventHandlerAdapterMaker {
    EventHandlerMethodAdapter make(Method method);
}

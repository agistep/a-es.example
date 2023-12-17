package io.agistep.event;

import java.lang.reflect.Method;

public interface EventHandlerAdapterInitializer {
    EventHandlerMethodAdapter init(Method method);
}

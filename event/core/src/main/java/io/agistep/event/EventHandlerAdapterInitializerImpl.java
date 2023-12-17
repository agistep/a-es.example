package io.agistep.event;

import java.lang.reflect.Method;

class EventHandlerAdapterInitializerImpl implements EventHandlerAdapterInitializer {
    @Override
    public EventHandlerMethodAdapter init(Method method) {
        return EventHandlerMethodAdapter.init(method);
    }
}

package io.agistep.event;

import java.lang.reflect.Method;

class EventHandlerAdapterMakerImpl implements EventHandlerAdapterMaker {
    @Override
    public EventHandlerMethodAdapter make(Method method) {
        return EventHandlerMethodAdapter.init(method);
    }
}

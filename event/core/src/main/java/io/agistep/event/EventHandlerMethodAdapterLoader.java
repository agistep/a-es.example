package io.agistep.event;

import java.lang.reflect.Method;
import java.util.List;

public class EventHandlerMethodAdapterLoader {

    private final EventHandlerMethodScanner eventHandlerScanner;
    private final EventHandlerAdapterInitializer eventHandlerAdapterInitializer;

    public EventHandlerMethodAdapterLoader(EventHandlerMethodScanner eventHandlerScanner, EventHandlerAdapterInitializer eventHandlerAdapterInitializer) {
        this.eventHandlerScanner = eventHandlerScanner;
        this.eventHandlerAdapterInitializer = eventHandlerAdapterInitializer;
    }

    List<EventHandlerMethodAdapter> load(String basePackage) {
        List<Method> methods = eventHandlerScanner.scan(basePackage);
        return methods.stream().map(eventHandlerAdapterInitializer::init)
                .toList();
    }
}

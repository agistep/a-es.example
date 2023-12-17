package io.agistep.event;

import java.lang.reflect.Method;
import java.util.List;

public class EventHandlerLoader {

    private final EventHandlerMethodScanner eventHandlerScanner;
    private final EventHandlerAdapterMaker eventHandlerAdapterMaker;

    public EventHandlerLoader(EventHandlerMethodScanner eventHandlerScanner, EventHandlerAdapterMaker eventHandlerAdapterMaker) {
        this.eventHandlerScanner = eventHandlerScanner;
        this.eventHandlerAdapterMaker = eventHandlerAdapterMaker;
    }

    List<EventHandlerMethodAdapter> load(String basePackage) {
        List<Method> methods = eventHandlerScanner.scan(basePackage);
        return methods.stream().map(method -> eventHandlerAdapterMaker.make(method))
                .toList();
    }
}

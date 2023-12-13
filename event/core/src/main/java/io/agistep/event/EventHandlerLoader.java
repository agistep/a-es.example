package io.agistep.event;

import io.agistep.utils.AnnotationHelper;
import io.agistep.utils.ScanClassProvider;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class EventHandlerLoader {
    private static Map<String, HandlerAdapter> handlers = new HashMap<>();

    static {
        try {
            // TODO: 2023/12/10 should scan from root package
            for (Class<?> cls : ScanClassProvider.scanAllClassesIn("io.agistep")) {
                for (Method method : cls.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(EventHandler.class)) {
                        var handler = method.getAnnotation(EventHandler.class);
                        var handlerName = handler.payload().getName();
                        handlers.put(handlerName, initHandler(cls.getDeclaredConstructor().newInstance()));
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize EventHandlerRegistry", e);
        }
    }

    private static HandlerAdapter initHandler(Object aggregate) {
        List<Method> eventHandlerMethods = AnnotationHelper.getMethodsListWithAnnotation(aggregate.getClass(), EventHandler.class);
        List<Pair<EventHandler, Method>> handlerMethodPairs = eventHandlerMethods.stream().map(m -> {
            EventHandler annotation = AnnotationHelper.getAnnotation(m, EventHandler.class);

            return Pair.of(annotation, m);
        }).collect(toList());

        return new HandlerAdapter(aggregate, handlerMethodPairs);
    }

    HandlerAdapter retrieveHandler(String name) {
        return handlers.get(name);
    }
}

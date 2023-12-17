package io.agistep.event;

import io.agistep.utils.ScanClassProvider;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EventHandlerLoader {
    private static Map<String, HandlerAdapter> handlers = new HashMap<>();

    static {
        try {
            // TODO: 2023/12/10 should scan from root package
            Set<Class<?>> scanned = ScanClassProvider.scanAllClassesIn("io.agistep");

            scanned.stream()
                    .flatMap(cls -> Arrays.stream(cls.getDeclaredMethods()))
                    .filter(method -> method.isAnnotationPresent(EventHandler.class))
                    .forEach(method -> {
                        var handler = method.getAnnotation(EventHandler.class);
                        var handlerName = handler.payload().getName();
                        handlers.put(handlerName, HandlerAdapter.init(method.getDeclaringClass()));
                    });

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize EventHandlerRegistry", e);
        }
    }

    HandlerAdapter retrieveHandler(String name) {
        return handlers.get(name);
    }
}

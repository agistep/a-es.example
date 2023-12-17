package io.agistep.event;

import io.agistep.utils.ScanClassProvider;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

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
                        handlers.put(handlerName, HandlerAdapter.init(cls.getDeclaredConstructor().newInstance()));
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize EventHandlerRegistry", e);
        }
    }

    HandlerAdapter retrieveHandler(String name) {
        return handlers.get(name);
    }
}

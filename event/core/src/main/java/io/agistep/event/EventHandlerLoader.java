package io.agistep.event;

import io.agistep.utils.BasePackageLoader;
import io.agistep.utils.ScanClassProvider;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EventHandlerLoader {
    private static final Map<String, HandlerAdapter> handlers = new HashMap<>();

    static {
        Set<Class<?>> scanned = ScanClassProvider.scanAllClassesIn(BasePackageLoader.load());

        scanned.stream()
                .flatMap(cls -> Arrays.stream(cls.getDeclaredMethods()))
                .filter(method -> method.isAnnotationPresent(EventHandler.class))
                .forEach(method -> {
                    var handler = method.getAnnotation(EventHandler.class);
                    var handlerName = handler.payload().getName();
                    handlers.put(handlerName, HandlerAdapter.init(method.getDeclaringClass()));
                });
    }

    HandlerAdapter retrieveHandler(String name) {
        return handlers.get(name);
    }
}

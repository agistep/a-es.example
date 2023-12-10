package io.agistep.event;

import io.agistep.utils.AnnotationHelper;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

import static java.util.stream.Collectors.toList;

class EventHandlerLoader {
    private static Map<String, HandlerAdapter> handlers = new HashMap<>();

    static {
        try {
            // TODO: 2023/12/10 should scan from root package 
            for (Class<?> cls : scanAndGetAllClasses("io.agistep.event")) {
                for (Method method : cls.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(EventHandler.class)) {
                        var handler = method.getAnnotation(EventHandler.class);
                        var handlerName = handler.payload().getName();
                        handlers.put(handlerName, initHandler(cls.newInstance()));
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize EventHandlerRegistry", e);
        }
    }

    public static Set<Class<?>> scanAndGetAllClasses(String packageName) throws IOException, ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        Set<File> dirs = new HashSet<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        Set<Class<?>> classes = new HashSet<>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes;
    }

    private static Set<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        Set<Class<?>> classes = new HashSet<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
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

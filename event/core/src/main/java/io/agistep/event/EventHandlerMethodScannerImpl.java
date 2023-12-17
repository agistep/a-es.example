package io.agistep.event;

import io.agistep.utils.BasePackageLoader;
import io.agistep.utils.ScanClassProvider;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

class EventHandlerMethodScannerImpl implements EventHandlerMethodScanner {

    @Override
    public List<Method> scan(String basePackage) {
        Set<Class<?>> scanned = ScanClassProvider.scanAllClassesIn(BasePackageLoader.load());

        return scanned.stream()
                .flatMap(cls -> Arrays.stream(cls.getDeclaredMethods()))
                .filter(method -> method.isAnnotationPresent(EventHandler.class))
                .toList();
    }
}

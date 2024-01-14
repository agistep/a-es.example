package io.agistep.event;

import java.lang.reflect.Method;
import java.util.List;

public interface EventHandlerMethodScanner {

    List<Method> scan(String basePackage);
}

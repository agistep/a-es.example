package io.agistep.utils;

import io.agistep.event.Event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class MethodHelper {

    public static void invoke(Object aggregate, Event anEvent, Method method) throws IllegalAccessException, InvocationTargetException {
        Parameter[] parameters = method.getParameters();

        if (parameters == null || parameters.length == 0) {
            return;
        }

        if (parameters.length > 1) {
            Class<?> secondParameterType = parameters[1].getType();

            if (Long.class.isAssignableFrom(secondParameterType)) {
                method.invoke(aggregate, anEvent.getPayload(), anEvent.getAggregateId());
                return;
            }
        }

        method.invoke(aggregate, anEvent);
    }
}

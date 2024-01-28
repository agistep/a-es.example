package io.agistep.utils;

import io.agistep.aggregator.Aggregate;
import io.agistep.event.Event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static org.valid4j.Validation.validate;

public class MethodInvokeHelper {

    public static Object invoke(Aggregate aggregate, Event anEvent, Method method) throws IllegalAccessException, InvocationTargetException {
        Parameter[] parameters = method.getParameters();
        validate(parameters != null && parameters.length > 0,
                new IllegalArgumentException(String.format("Method %s has no parameters", method.getName())));

        if (parameters.length == 1) {
            Class<?> firstParameterType = parameters[0].getType();

            if (Event.class.isAssignableFrom(firstParameterType)) {
                return method.invoke(aggregate, anEvent);
            } else {
                return method.invoke(aggregate, anEvent.getPayload());
            }
        }

        Class<?> secondParameterType = parameters[1].getType();
        if (Long.class.isAssignableFrom(secondParameterType)) {
            return method.invoke(aggregate, anEvent.getPayload(), anEvent.getAggregateId());
        }

        throw new IllegalArgumentException(String.format("Method %s has invalid parameters", method.getName()));
    }
}

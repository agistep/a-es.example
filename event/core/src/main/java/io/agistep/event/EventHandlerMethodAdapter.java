package io.agistep.event;

import io.agistep.utils.AnnotationHelper;
import io.agistep.utils.MethodInvokeHelper;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.valid4j.Validation.validate;

class EventHandlerMethodAdapter {

	private final String aggregateName;
	private final Pair<EventHandler, Method> handlerMethodPair;

	EventHandlerMethodAdapter(Class<?> aggregateClass, Pair<EventHandler, Method> handlerMethodPair) {
		this.aggregateName = aggregateClass.getName();
		this.handlerMethodPair = handlerMethodPair;
	}

	public static EventHandlerMethodAdapter init(Method method) {
		validate(method != null, new IllegalArgumentException("Method is null"));

		EventHandler annotation = AnnotationHelper.getAnnotation(method, EventHandler.class);
		validate(annotation != null, new IllegalArgumentException("EventHandler annotation is not present on method: " + method.getName()));

		Pair<EventHandler, Method> eventHandlerMethodPair = Pair.of(annotation, method);
		return new EventHandlerMethodAdapter(method.getDeclaringClass(), eventHandlerMethodPair);
	}

	String getAggregateName() {
		return aggregateName;
	}

	public void handle(Object aggregate, Event anEvent) {
		try {
			Method method = handlerMethodPair.getValue();
			method.setAccessible(true);

			MethodInvokeHelper.invoke(aggregate, anEvent, method);
		} catch (InvocationTargetException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	String getPayloadName() {
		return this.handlerMethodPair.getKey().payload().getName();
	}
}

package io.agistep.event;

import io.agistep.utils.AnnotationHelper;
import io.agistep.utils.MethodInvokeHelper;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.NoSuchElementException;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;


class EventHandlerMethodAdapter {

	private final String aggregateName;
	private final List<Pair<EventHandler, Method>> handlerMethods;

	EventHandlerMethodAdapter(Object aggregate, List<Pair<EventHandler, Method>> handlerMethods) {
		this(aggregate.getClass(), handlerMethods);
	}

	EventHandlerMethodAdapter(Class<?> aggregateClass, List<Pair<EventHandler, Method>> handlerMethods) {
		this(aggregateClass.getName(), handlerMethods);
	}

	private EventHandlerMethodAdapter(String aggregateName, List<Pair<EventHandler, Method>> handlerMethods) {
		this.aggregateName = aggregateName;
		this.handlerMethods = handlerMethods;
	}

	public static EventHandlerMethodAdapter init(Class<?> aggregateClass) {
		List<Method> eventHandlerMethods = AnnotationHelper.getMethodsListWithAnnotation(aggregateClass, EventHandler.class);
		List<Pair<EventHandler, Method>> handlerMethodPairs = eventHandlerMethods.stream().map(m -> {
			EventHandler annotation = AnnotationHelper.getAnnotation(m, EventHandler.class);

			return Pair.of(annotation, m);
		}).collect(toList());

		return new EventHandlerMethodAdapter(aggregateClass, handlerMethodPairs);
	}


	public static EventHandlerMethodAdapter init(Method method) {
			EventHandler annotation = AnnotationHelper.getAnnotation(method, EventHandler.class);

		Pair<EventHandler, Method> eventHandlerMethodPair = Pair.of(annotation, method);
		return new EventHandlerMethodAdapter(method.getDeclaringClass(), List.of(eventHandlerMethodPair));
	}

	String getAggregateName() {
		return aggregateName;
	}

	public void handle(Object aggregate, Event anEvent) {
		String eventName = anEvent.getName();

		Pair<EventHandler, Method> handlerMethodPair = handlerMethods.stream()
				.filter(hm -> hm.getKey().payload().getName().equals(eventName))
				.findFirst()
				.orElseThrow(()->
						new NoSuchElementException(format("No Handler for %s Present in %s.", eventName, aggregate.getClass().getName())));

		try {
			Method method = handlerMethodPair.getValue();
			method.setAccessible(true);

			MethodInvokeHelper.invoke(aggregate, anEvent, method);
		} catch (InvocationTargetException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	Pair<EventHandler, Method> getPair(int index) {
		return this.handlerMethods.get(index);
	}

	String getPayloadName() {
		return this.getPair(0).getKey().payload().getName();
	}
}

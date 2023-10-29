package io.agistep.event;

import io.agistep.utils.AnnotationHelper;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public final class EventReorganizor {

	public static void reorganize(Object aggregate, Event[] events) {
		Arrays.stream(events)
				.forEach(e-> EventReorganizor.reorganize(aggregate, e));
	}

	public static void reorganize(Object aggregate, Event anEvent) {
		HandlerAdapter handler = findHandler(aggregate);
		handler.handle(aggregate, anEvent);

		ThreadLocalEventVersionHolder.instance().setVersion(aggregate, anEvent);
	}

	private static HandlerAdapter findHandler(Object aggregate) {
		final String aggregateName = aggregate.getClass().getName();
		HandlerAdapter handler = retrieveHandler(aggregateName);
		if(handler != null) {
			return handler;
		}

		return caching(initHandler(aggregate));

	}

	private static HandlerAdapter initHandler(Object aggregate) {
		final String aggregateName = aggregate.getClass().getName();
		List<Method> eventHandlerMethods = AnnotationHelper.getMethodsListWithAnnotation(aggregate.getClass(), EventHandler.class);
		List<Pair<EventHandler, Method>> aa = eventHandlerMethods.stream().map(m -> {
			EventHandler annotation = AnnotationHelper.getAnnotation(m, EventHandler.class);

			return Pair.of(annotation, m);
		}).collect(toList());

		return new HandlerAdapter(aggregateName, aa);
	}

	final static Map<String, HandlerAdapter> handlers = new HashMap<>();

	private static HandlerAdapter retrieveHandler(String aggregateName) {
		return handlers.get(aggregateName);
	}

	private static HandlerAdapter caching(HandlerAdapter handler) {
		handlers.put(handler.getAggregateName(), handler);
		return handler;
	}
}

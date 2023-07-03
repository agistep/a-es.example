package io.agistep.event;

import io.agistep.utils.AnnotationHelper;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class EventReorganizer {
	public static void reorganize(Object aggregate, Event anEvent) {
		Handler handler = findHandler(aggregate);
		handler.handle(aggregate, anEvent);

		// TODO throw new UnsupportedOperationException();
	}

	private static Handler findHandler(Object aggregate) {
		final String aggregateName = aggregate.getClass().getName();
		Handler handler = retrieveHandler(aggregateName);
		if(handler != null) {
			return handler;
		}

		return caching(initHandler(aggregate));

	}

	private static Handler initHandler(Object aggregate) {
		final String aggregateName = aggregate.getClass().getName();
		List<Method> eventHandlerMethods = AnnotationHelper.getMethodsListWithAnnotation(aggregate.getClass(), EventHandler.class);
		List<Pair<EventHandler, Method>> aa = eventHandlerMethods.stream().map(m -> {
			EventHandler annotation = AnnotationHelper.getAnnotation(m, EventHandler.class);

			return Pair.of(annotation, m);
		}).collect(toList());

		return new Handler(aggregateName, aa);
	}

	final static Map<String, Handler> handlers = new HashMap<>();

	private static Handler retrieveHandler(String aggregateName) {
		return handlers.get(aggregateName);
	}

	private static Handler caching(Handler handler) {
		handlers.put(handler.getAggregateName(), handler);
		return handler;
	}
}

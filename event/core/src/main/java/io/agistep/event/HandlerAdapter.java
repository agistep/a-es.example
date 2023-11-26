package io.agistep.event;

import io.agistep.utils.MethodHelper;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;


class HandlerAdapter {

	private final String aggregateName;
	private final List<Pair<EventHandler, Method>> handlerMethods;

	public HandlerAdapter(String aggregateName, List<Pair<EventHandler, Method>> handlerMethods) {
		this.aggregateName = aggregateName;
		this.handlerMethods = handlerMethods;
	}

	String getAggregateName() {
		return aggregateName;
	}

	public void handle(Object aggregate, Event anEvent) {
		String eventName = anEvent.getName();

		Pair<EventHandler, Method> handlerMethodPair = handlerMethods.stream()
				.filter(hm -> hm.getKey().payload().getName().equals(eventName))
				.findFirst().get();

		try {
			Method method = handlerMethodPair.getValue();
			method.setAccessible(true);

			MethodHelper.invoke(aggregate, anEvent, method);
		} catch (InvocationTargetException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}

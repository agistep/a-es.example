package io.agistep.event;

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

		Pair<EventHandler, Method> aa = handlerMethods.stream().filter(hm -> hm.getKey().payload().getName().equals(eventName)).findFirst().get();

		try {
			Method method = aa.getValue();
			method.setAccessible(true);
			method.invoke(aggregate, anEvent);
		} catch (InvocationTargetException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}

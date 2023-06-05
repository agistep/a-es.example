package io.agistep.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class SimpleDomainEventApplier implements DomainEventApplier {

	public static DomainEventApplier instance() {
		return new SimpleDomainEventApplier();
	}

	private SimpleDomainEventApplier() {
	}

	@Override
	public void replay(Object aggregate, Event anEvent) {
		final String className = aggregate.getClass().getName();
		if("io.agistep.todo.domain.Todo".equals(className)) {
			final String eventName = anEvent.getClass().getName();
			if ("io.agistep.todo.domain.TodoCreated".equals(eventName)) {
				final String handlerName = "onCreated";
				handle(handlerName, aggregate,anEvent);
				return;
			}
			if ("io.agistep.todo.domain.TodoDone".equals(eventName)) {
				final String handlerName = "onDone";
				handle(handlerName, aggregate,anEvent);
				return;
			}
			if ("io.agistep.todo.domain.TodoTextUpdated".equals(eventName)) {
				final String handlerName = "onTextUpdated";
				handle(handlerName, aggregate,anEvent);
				return;
			}
			if ("io.agistep.todo.domain.TodoHeld".equals(eventName)) {
				final String handlerName = "onHeld";
				handle(handlerName, aggregate,anEvent);
				return;
			}
		}

		throw new UnsupportedOperationException();
	}

	private static void handle(String handlerName, Object aggregate, Event anEvent) {
		try {
			Method method = aggregate.getClass().getDeclaredMethod(handlerName, anEvent.getClass());
			method.setAccessible(true);
			method.invoke(aggregate, anEvent);
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void occurred(Event anEvent) {
		EventList.instance().occurs(anEvent);
	}

}

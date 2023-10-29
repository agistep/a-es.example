package io.agistep.todo.domain;

import io.agistep.event.Event;
import io.agistep.event.EventHolder;
import io.agistep.event.storages.EventStorage;

import java.util.List;
import java.util.Optional;

import static org.valid4j.Assertive.require;

class EventStoreTodoRepository implements TodoRepository {

	private final EventStorage eventStorage;

	public EventStoreTodoRepository(EventStorage eventStorage) {
		require(eventStorage != null);
		this.eventStorage = eventStorage;
	}

	@Override
	public void save(Todo todo) {
		List<Event> events = EventHolder.instance().getEvents(todo);
		eventStorage.save(events);
	}

	@Override
	public Optional<Todo> findBy(long todoId) {
		List<Event> byId = eventStorage.findById(todoId);
		return Optional.ofNullable(Todo.reorganize(byId));
	}
}

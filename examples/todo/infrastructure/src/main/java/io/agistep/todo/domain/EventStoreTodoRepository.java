package io.agistep.todo.domain;

import io.agistep.event.Event;
import io.agistep.event.EventStore;

import java.util.List;
import java.util.Optional;

import static org.valid4j.Assertive.require;

class EventStoreTodoRepository implements TodoRepository {
	private final EventStore eventStore;

	public EventStoreTodoRepository(EventStore eventStore) {
		require(eventStore != null);
		this.eventStore = eventStore;
	}

	@Override
	public void save(Todo todo) {
		//TODO save 에 대한 exception
		eventStore.publishOccurredEventOf(todo);
	}

	@Override
	public Optional<Todo> findBy(long todoId) {
		List<Event> events = eventStore.load(todoId);
		return Optional.ofNullable(Todo.reorganize(events));
	}
}

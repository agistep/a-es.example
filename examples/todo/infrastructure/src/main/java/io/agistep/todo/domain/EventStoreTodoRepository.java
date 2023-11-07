package io.agistep.todo.domain;

import io.agistep.event.repository.SimpleAggregateRepository;
import io.agistep.event.storages.EventStorage;

import java.util.Optional;

class EventStoreTodoRepository extends SimpleAggregateRepository<Todo> implements TodoRepository {

	public EventStoreTodoRepository(EventStorage eventStorage) {
		super(Todo::new, eventStorage);
	}

	@Override
	public Optional<Todo> findBy(long todoId) {
		return this.findById(todoId);
	}
}

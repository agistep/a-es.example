package io.agistep.todo.applications;

import io.agistep.event.EventList;
import io.agistep.event.EventStore;
import io.agistep.todo.domain.Todo;
import io.agistep.todo.domain.TodoFactory;
import io.agistep.todo.usecase.TodoCreationUseCase;

class TodoCreationApplicationService implements TodoCreationUseCase {
	private final TodoFactory todoFactory;
	private final EventStore eventStore;

	public TodoCreationApplicationService(TodoFactory todoFactory, EventStore eventStore) {
		this.todoFactory = todoFactory;
		this.eventStore = eventStore;
	}

	@Override
	public void create(String todoText) {
		Todo todo = todoFactory.create(todoText);

		eventStore.publish(EventList.instance().occurredListBy(todo).get(0));
	}
}

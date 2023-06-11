package io.agistep.todo.applications;

import io.agistep.todo.domain.Todo;
import io.agistep.todo.domain.TodoFactory;
import io.agistep.todo.domain.TodoRepository;
import io.agistep.todo.usecase.TodoCreationUseCase;

class TodoCreationApplicationService implements TodoCreationUseCase {
	private final TodoFactory todoFactory;
	private final TodoRepository todoRepository;

	public TodoCreationApplicationService(TodoFactory todoFactory, TodoRepository todoRepository) {
		this.todoFactory = todoFactory;
		this.todoRepository = todoRepository;

	}

	@Override
	public void create(String todoText) {
		Todo todo = todoFactory.create(todoText);
		todoRepository.save(todo);
	}
}

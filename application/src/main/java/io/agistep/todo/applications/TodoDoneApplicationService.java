package io.agistep.todo.applications;

import io.agistep.todo.domain.Todo;
import io.agistep.todo.domain.TodoRepository;
import io.agistep.todo.usecase.TodoDoneUseCase;

import java.util.NoSuchElementException;
import java.util.Optional;

class TodoDoneApplicationService implements TodoDoneUseCase {

	private final TodoRepository repository;

	public TodoDoneApplicationService(TodoRepository repository) {
		this.repository = repository;
	}

	@Override
	public void done(long todoId) {
		Optional<Todo> todo = repository.findBy(todoId);

		if (todo.isEmpty()) {
			throw new NoSuchElementException();
		}
		todo.ifPresent(repository::save);
	}
}

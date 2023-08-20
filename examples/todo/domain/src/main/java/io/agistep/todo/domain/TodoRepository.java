package io.agistep.todo.domain;

import java.util.Optional;

public interface TodoRepository {
	void save(Todo todo);

	Optional<Todo> findBy(long todoId);
}

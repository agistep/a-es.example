package io.agistep.todo.domain;

public class TodoFactory {
	public Todo create(String text) {
		return new Todo(text);
	}
}

package io.agistep.todo.domain;

import io.agistep.event.AbstractEvent;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = true)
class TodoCreated extends AbstractEvent {
	@Getter
	private final String text;

	public TodoCreated(long aggregateIdValue, String text) {
		super(aggregateIdValue);
		this.text = text;
	}
}

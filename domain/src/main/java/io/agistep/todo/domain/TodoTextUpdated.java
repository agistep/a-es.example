package io.agistep.todo.domain;

import io.agistep.event.AbstractEvent;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = true)
class TodoTextUpdated extends AbstractEvent {
	@Getter
	private final String updatedText;

	public TodoTextUpdated(long aggregateIdValue, String updatedText) {
		super(aggregateIdValue);
		this.updatedText = updatedText;
	}
}

package io.agistep.todo.domain;

import io.agistep.event.AbstractEvent;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
class TodoHeld extends AbstractEvent {

	public TodoHeld(long aggregateIdValue) {
		super(aggregateIdValue);
	}
}

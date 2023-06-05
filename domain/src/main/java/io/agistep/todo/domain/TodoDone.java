package io.agistep.todo.domain;


import io.agistep.event.AbstractEvent;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
public class TodoDone extends AbstractEvent {
	public TodoDone(long aggregateIdValue) {
		super(aggregateIdValue);
	}
}

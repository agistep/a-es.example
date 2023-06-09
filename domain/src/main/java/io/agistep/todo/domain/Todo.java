package io.agistep.todo.domain;

import io.agistep.event.DomainEventApplier;
import io.agistep.event.Event;
import io.agistep.event.Events;
import lombok.Getter;

import java.util.Arrays;


@Getter
public class Todo {

	public static Todo replay(Event... events) {
		return new Todo(events);
	}

	private TodoIdentity id;
	private String text;
	private boolean done;
	private boolean hold;

	Todo(String text) {
		Event anEvent = Events.begin(new TodoCreated(text));
		DomainEventApplier.instance().apply(this, anEvent);
	}

	private Todo(Event... anEvent) {
		Arrays.stream(anEvent).forEach(e -> {
			DomainEventApplier.instance().replay(this, e);
		});
	}

	void onCreated(Event anEvent) {
		this.id = new TodoIdentity(anEvent.getAggregateIdValue());
		this.text = ((TodoCreated) anEvent.getPayload()).getText();
		this.done = false;
	}

	public void done() {
		if(isDone()) {
			return;
		}
		Event anEvent = Events.occurs(this, new TodoDone());
		DomainEventApplier.instance().apply(this, anEvent);
	}

	void onDone(Event anEvent) {
		this.done = true;
	}

	public void updateText(String text) {
		Event anEvent = Events.occurs(this, new TodoTextUpdated(text));
		DomainEventApplier.instance().apply(this, anEvent);
	}

	void onTextUpdated(Event anEvent) {
		this.text = ((TodoTextUpdated) anEvent.getPayload()).getUpdatedText();
	}

	public void hold() {
		if (isDone()) {
			return;
		}
		Event anEvent = Events.occurs(id.getValue(), new TodoHeld());
		DomainEventApplier.instance().apply(this, anEvent);
	}

	void onHeld(Event anEvent) {
		this.hold = true;
	}


}

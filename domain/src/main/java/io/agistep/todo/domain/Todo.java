package io.agistep.todo.domain;

import io.agistep.annotation.EventSourcingAggregate;
import io.agistep.event.Event;
import io.agistep.event.EventHandler;
import lombok.Getter;

@Getter
@EventSourcingAggregate
public class Todo {

	private TodoIdentity id;
	private String text;
	private boolean done;
	private boolean hold;

	public Todo() {
	}

	Todo(String text) {
		TodoCreated created = TodoCreated.newBuilder()
				.setText(text)
				.build();
		apply(created);
	}
  
	@EventHandler(payload = TodoCreated.class)
	void onCreated(Event anEvent) {
		this.id = new TodoIdentity(anEvent.getAggregateIdValue());
		this.text = ((TodoCreated) anEvent.getPayload()).getText();
		this.done = false;
	}

	public void done() {
		if(isDone()) {
			return;
		}
		TodoDone todoDone = TodoDone.newBuilder().build();
		apply(todoDone);
	}

	@EventHandler(payload = TodoDone.class)
	void onDone(Event anEvent) {
		this.done = true;
	}

	public void updateText(String text) {
		apply(TodoTextUpdated.newBuilder().setUpdatedText(text).build());
	}

	@EventHandler(payload = TodoTextUpdated.class)
	void onTextUpdated(Event anEvent) {
		this.text = ((TodoTextUpdated) anEvent.getPayload()).getUpdatedText();
	}

	public void hold() {
		if (isDone()) {
			return;
		}
		apply(TodoHeld.newBuilder().build());
	}

	@EventHandler(payload = TodoHeld.class)
	void onHeld(Event anEvent) {
		this.hold = true;
	}
}

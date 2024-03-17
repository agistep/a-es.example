package io.agistep.todo.domain;

import io.agistep.event.Event;
import io.agistep.event.EventHandler;
import io.agistep.event.EventSource;
import lombok.Getter;


@Getter
public class Todo {

	private long id;
	private String text;
	private boolean done;
	private boolean hold;

	//post construct at replay
	public Todo() {
	}

	//pre construct
	Todo(String text) {
		TodoCreated created = TodoCreated.newBuilder()
				.setText(text)
				.build();
		EventSource.apply(this, created);
	}

	@EventHandler(payload = TodoCreated.class)
	void onCreated(TodoCreated created, Long aggregateId) {
		this.id = aggregateId;
		this.text = created.getText();
		this.done = false;
	}

	public void done() {
		if(isDone()) {
			return;
		}
		Object payload = TodoDone.newBuilder().build();
		EventSource.apply(this, payload);
	}

	@EventHandler(payload = TodoDone.class)
	void onDone(TodoDone done) {
		this.done = true;
	}

	public void updateText(String text) {
		Object payload = TodoTextUpdated.newBuilder().setUpdatedText(text).build();
		EventSource.apply(this, payload);
	}

	@EventHandler(payload = TodoTextUpdated.class)
	void onTextUpdated(TodoTextUpdated payload) {
		this.text = payload.getUpdatedText();
	}

	public void hold() {
		if (isDone()) {
			return;
		}
		Object payload = TodoHeld.newBuilder().build();
		EventSource.apply(this, payload);
	}

	@EventHandler(payload = TodoHeld.class)
	void onHeld(Event anEvent) {
		this.hold = true;
	}

}

package io.agistep.todo.domain;

import io.agistep.event.Event;
import io.agistep.event.EventApplier;
import io.agistep.event.EventHandler;
import lombok.Getter;


@Getter
public class Todo {

	private TodoIdentity id;
	private String text;
	private boolean done;
	private boolean hold;

	//post construct at reorganize
	public Todo() {
	}

	//pre construct
	Todo(String text) {
		TodoCreated created = TodoCreated.newBuilder()
				.setText(text)
				.build();
		EventApplier.instance().apply(this, created);
	}

	@EventHandler(payload = TodoCreated.class)
	void onCreated(Event anEvent) {
		this.id = new TodoIdentity(anEvent.getAggregateId());
		this.text = ((TodoCreated) anEvent.getPayload()).getText();
		this.done = false;
	}

	public void done() {
		if(isDone()) {
			return;
		}
		Object payload = TodoDone.newBuilder().build();
		EventApplier.instance().apply(this, payload);
	}

	@EventHandler(payload = TodoDone.class)
	void onDone(Event anEvent) {
		this.done = true;
	}

	public void updateText(String text) {
		Object payload = TodoTextUpdated.newBuilder().setUpdatedText(text).build();
		EventApplier.instance().apply(this, payload);
	}

	@EventHandler(payload = TodoTextUpdated.class)
	void onTextUpdated(Event anEvent) {
		this.text = ((TodoTextUpdated) anEvent.getPayload()).getUpdatedText();
	}

	public void hold() {
		if (isDone()) {
			return;
		}
		Object payload = TodoHeld.newBuilder().build();
		EventApplier.instance().apply(this, payload);
	}

	@EventHandler(payload = TodoHeld.class)
	void onHeld(Event anEvent) {
		this.hold = true;
	}


}

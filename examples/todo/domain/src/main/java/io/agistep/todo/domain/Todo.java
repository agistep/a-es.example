package io.agistep.todo.domain;

import io.agistep.event.Event;
import io.agistep.event.EventApplier;
import io.agistep.event.EventHandler;
import io.agistep.event.EventReorganizer;
import lombok.Getter;

import java.util.List;


@Getter
public class Todo {

	public static Todo reorganize(List<Event> events) {
		if(events == null || events.isEmpty()) {
			return null;
		}
		return reorganize(events.toArray(new Event[0]));
	}

	public static Todo reorganize(Event... events) {
		Todo aggregate = new Todo();

		if(events == null || events.length == 0) {
			return null;
		}
		EventReorganizer.reorganize(aggregate, events);
		return aggregate;
	}


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
		EventApplier.instance().apply(this, created);
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
		EventApplier.instance().apply(this, TodoDone.newBuilder().build());
	}

	@EventHandler(payload = TodoDone.class)
	void onDone(Event anEvent) {
		this.done = true;
	}

	public void updateText(String text) {
		EventApplier.instance().apply(this, TodoTextUpdated.newBuilder().setUpdatedText(text).build());
	}

	@EventHandler(payload = TodoTextUpdated.class)
	void onTextUpdated(Event anEvent) {
		this.text = ((TodoTextUpdated) anEvent.getPayload()).getUpdatedText();
	}

	public void hold() {
		if (isDone()) {
			return;
		}
		EventApplier.instance().apply(this, TodoHeld.newBuilder().build());
	}

	@EventHandler(payload = TodoHeld.class)
	void onHeld(Event anEvent) {
		this.hold = true;
	}


}

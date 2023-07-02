package io.agistep.todo.domain;

import io.agistep.event.Event;
import io.agistep.event.EventApplier;
import io.agistep.event.EventHandler;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;


@Getter
public class Todo {

	public static Todo replay(List<Event> events) {
		if(events == null || events.isEmpty()) {
			return null;
		}
		return replay(events.toArray(new Event[0]));
	}

	public static Todo replay(Event... events) {
		if(events == null || events.length == 0) {
			return null;
		}
		return new Todo(events);
	}


	private TodoIdentity id;
	private String text;
	private boolean done;
	private boolean hold;

	Todo(String text) {
		TodoCreated created = TodoCreated.newBuilder()
				.setText(text)
				.build();
		EventApplier.instance().apply(this, created);
	}

	private Todo(Event... anEvent) {
		Arrays.stream(anEvent).forEach(e -> {
			EventApplier.instance().replay(this, e);
		});
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

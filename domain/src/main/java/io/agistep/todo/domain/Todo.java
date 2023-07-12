package io.agistep.todo.domain;

import com.google.protobuf.Message;
import io.agistep.annotation.EventSourcingAggregate;
import io.agistep.event.Event;
import io.agistep.event.EventApplier;
import io.agistep.event.EventHandler;
import io.agistep.event.EventReorganizer;
import lombok.Getter;

import java.util.List;


@Getter
@EventSourcingAggregate
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

	private void apply(Message message) {
		EventApplier.instance().apply(this, message);
	}


//	public Todo() {
//	}

	private TodoIdentity id;
	private String text;
	private boolean done;
	private boolean hold;


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

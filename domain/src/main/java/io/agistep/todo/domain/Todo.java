package io.agistep.todo.domain;

import io.agistep.event.DomainEventApplier;
import io.agistep.event.Event;
import io.agistep.identity.IdentityValueProvider;
import lombok.Getter;

import java.util.Arrays;


@Getter
public class Todo {

	public static Todo replay(Event ... events) {
		return new Todo(events);
	}

	private TodoIdentity id;
	private String text;
	private boolean done;
	private boolean hold;

	Todo(String text) {
		long idValue = IdentityValueProvider.instance().newLong();
		TodoCreated anEvent = new TodoCreated(idValue, text);
		DomainEventApplier.instance().apply(this, anEvent);
	}

	 private Todo(Event... anEvent) {
		Arrays.stream(anEvent).forEach(e-> {
			DomainEventApplier.instance().replay(this, e);
		});
	}

	void onCreated(TodoCreated anEvent){
		this.id = new TodoIdentity(anEvent.getAggregateIdValue());
		this.text= anEvent.getText();
		this.done = false;
	}

	public void done() {
		DomainEventApplier.instance().apply(this, new TodoDone(this.getId().getValue()));
	}

	void onDone(TodoDone anEvent) {
		this.done = true;
	}

	public void updateText(String text) {
		DomainEventApplier.instance().apply(this, new TodoTextUpdated(this.getId().getValue(), text));
	}

	void onTextUpdated(TodoTextUpdated anEvent) {
		this.text = anEvent.getUpdatedText();
	}

	public void hold() {
		if(isDone()) {
			return;
		}
		DomainEventApplier.instance().apply(this,new TodoHeld(this.getId().getValue()));
	}

	void onHeld(TodoHeld anEvent) {
		this.hold = true;
	}


}

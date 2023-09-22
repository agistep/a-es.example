package io.agistep.todo.domain;

import io.agistep.event.Event;
import io.agistep.event.EventApplier;
import io.agistep.event.EventHandler;
import io.agistep.event.EventReorganizer;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Getter
public class Todo {

	private Map<Long, Long> aggregateOrderMap;

	public static Todo reorganize(List<Event> events) {
		if(events == null || events.isEmpty()) {
			return null;
		}
		return reorganize(events.toArray(new Event[0]));
	}

	public static Todo reorganize(Event... events) {
		long aggregateID = events[0].getAggregateIdValue();
		long latestOrder = events.length;
		Map<Long,Long> aggregateOrderMap = new HashMap<Long, Long>();
		aggregateOrderMap.put(aggregateID, latestOrder);

		Todo aggregate = new Todo(aggregateOrderMap);

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

	//post construct at reorganize
	public Todo(Map<Long,Long> aggregateOrderMap) {
		// reorganize
		this.aggregateOrderMap = aggregateOrderMap;
	}

	//pre construct
	Todo(String text) {
		TodoCreated created = TodoCreated.newBuilder()
				.setText(text)
				.build();
		this.aggregateOrderMap = EventApplier.instance().init(this, created);
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
		Object payload = TodoDone.newBuilder().build();
		EventApplier.instance().apply(this, payload, aggregateOrderMap);
	}

	@EventHandler(payload = TodoDone.class)
	void onDone(Event anEvent) {
		this.done = true;
	}

	public void updateText(String text) {
		Object payload = TodoTextUpdated.newBuilder().setUpdatedText(text).build();
		EventApplier.instance().apply(this, payload, new HashMap<>());
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
		EventApplier.instance().apply(this, payload, aggregateOrderMap);
	}

	@EventHandler(payload = TodoHeld.class)
	void onHeld(Event anEvent) {
		this.hold = true;
	}


}

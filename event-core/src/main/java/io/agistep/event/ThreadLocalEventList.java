package io.agistep.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Collections.unmodifiableList;

class ThreadLocalEventList implements EventList {

	private final static ThreadLocal<List<Event>> changes = new ThreadLocal<>();

	static EventList instance() {
		return new ThreadLocalEventList();
	}

	private ThreadLocalEventList() {
		// Do Nothing
	}

	@Override
	public List<Event> occurredListAll() {
		return isEmpty(changes.get()) ? List.of() : unmodifiableList(changes.get());
	}

	@Override
	public void occurs(Event anEvent) {
		List<Event> events = changes.get();

		if (isEmpty(events)) {
			events = init();
		}

		events.add(anEvent);
	}

	private static List<Event> init() {
		changes.set(new ArrayList<>());
		return new ArrayList<>();
	}

	private static boolean isEmpty(List<Event> events) {
		return events == null;
	}

	@Override
	public List<Event> occurredListBy(Object aggregate) {
		long idValue = AggregateSupports.getId(aggregate);
		return occurredListAll().stream().filter(e-> Objects.equals(idValue, e.getAggregateIdValue())).collect(Collectors.<Event>toList());
	}
}

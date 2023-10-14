package io.agistep.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Collections.synchronizedList;
import static java.util.Collections.unmodifiableList;

class ThreadLocalEventHolder implements EventHolder {

	private final static ThreadLocal<List<Event>> changes = new ThreadLocal<>();

	static EventHolder instance() {
		return new ThreadLocalEventHolder();
	}

	private ThreadLocalEventHolder() {
		// Do Nothing
	}

	@Override
	public List<Event> getEventAll() {
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
		changes.set(synchronizedList(new ArrayList<>()));
		return changes.get();
	}

	private static boolean isEmpty(List<Event> events) {
		return events == null || events.isEmpty();
	}

	@Override
	public List<Event> getEvents(Object aggregate) {
		long idValue = AggregateIdUtils.getIdFrom(aggregate);
		return getEventAll().stream().filter(e-> Objects.equals(idValue, e.getAggregateId())).collect(Collectors.<Event>toList());
	}

	@Override
	public void clearAll() {
		// TODO 이벤트 스토어에 저장 요청
		changes.remove();

		ThreadLocalEventVersionHolder.instance().clearAll();

	}

	@Override
	public void clear(Object aggregate) {
		final long id = AggregateIdUtils.getIdFrom(aggregate);

		List<Event> events = changes.get().stream()
				.filter(e -> e.getAggregateId() == id).collect(Collectors.toList());

		List<Event> remained = changes.get().stream().filter(e -> !events.contains(e)).collect(Collectors.toList());
		changes.set(remained);

		ThreadLocalEventVersionHolder.instance().clear(aggregate);
	}
}

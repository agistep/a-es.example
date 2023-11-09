package io.agistep.event;

import io.agistep.aggregator.IdUtils;

import java.util.ArrayList;
import java.util.Collections;
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
	public void hold(Event anEvent) {
		List<Event> events = changes.get();

		if (isEmpty(events)) {
			events = init();
		}

		events.add(anEvent);
		Events.updateVersion((Long)anEvent.getAggregateId(), anEvent.getVersion());
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
		if(IdUtils.notAssignedIdOf(aggregate)) {
			return Collections.EMPTY_LIST;
		}
		Object idValue = IdUtils.idOf(aggregate);
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
        final long id = IdUtils.idOf(aggregate);

		List<Event> events = changes.get().stream()
				.filter(e -> e.getAggregateId() == id).toList();

		List<Event> remained = changes.get().stream().filter(e -> !events.contains(e)).collect(Collectors.toList());
		changes.set(remained);

		ThreadLocalEventVersionHolder.instance().clear(aggregate);
	}
}

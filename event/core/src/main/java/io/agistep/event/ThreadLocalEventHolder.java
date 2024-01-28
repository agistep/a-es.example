package io.agistep.event;

import io.agistep.aggregator.IdUtils;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.ThreadLocal.withInitial;
import static java.util.Collections.synchronizedList;
import static java.util.Collections.unmodifiableList;

class ThreadLocalEventHolder implements EventHolder {

	private final static ThreadLocal<List<Event>> changes = withInitial(() -> synchronizedList(new ArrayList<>()));
	private final static ThreadLocal<Map<Long,Long>> changes2 = withInitial(HashMap::new);


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

		events.add(anEvent);
		updateSeq(anEvent.getAggregateId(), anEvent.getSeq());
	}


	private static boolean isEmpty(List<Event> events) {
		return events == null || events.isEmpty();
	}

	@Override
	public List<Event> getEvents(Object aggregate) {
		if(IdUtils.notAssignedIdOf(aggregate)) {
			return List.of();
		}
		Object idValue = IdUtils.idOf(aggregate);
		return getEventAll().stream().filter(e-> Objects.equals(idValue, e.getAggregateId())).collect(Collectors.<Event>toList());
	}

	@Override
	public void clearAll() {
		// TODO 이벤트 스토어에 저장 요청
		changes.remove();

		ThreadLocalEventSeqHolder.instance().clearAll();

	}

	@Override
	public void clear(Object aggregate) {
        final long id = IdUtils.idOf(aggregate);

		List<Event> events = changes.get().stream()
				.filter(e -> e.getAggregateId() == id).toList();

		List<Event> remained = changes.get().stream().filter(e -> !events.contains(e)).collect(Collectors.toList());
		changes.set(remained);

		ThreadLocalEventSeqHolder.instance().clear(aggregate);
	}

	private static void updateSeq(long aggregateId, long seq) {
		ThreadLocalEventSeqHolder.instance().setSeq(aggregateId, seq);
	}
}

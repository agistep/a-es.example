package io.agistep.event;

import java.util.List;

public interface EventStore {
	void publishOccurredEventOf(Object aggregate);

	default List<Event> load(long todoId) {
		return null;
	}
}

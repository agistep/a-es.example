package io.agistep.event;

import java.util.List;

@Deprecated
public interface EventStore {
	void publishOccurredEventOf(Object aggregate);

	default List<Event> load(long todoId) {
		return null;
	}
}

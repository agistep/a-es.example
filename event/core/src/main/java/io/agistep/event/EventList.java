package io.agistep.event;


import java.util.List;

public interface EventList {

	static EventList instance() {
		return ThreadLocalEventList.instance();
	}

	List<Event> occurredListAll();

	void occurs(Event anEvent);

	List<Event> occurredListBy(Object aggregate);

	void clear();

	default boolean hasEventsOf(Object aggregate) {
		return !occurredListBy(aggregate).isEmpty();
	}

	default long getLatestVersionOf(Object aggregate) {
		return this.occurredListBy(aggregate).get(this.occurredListBy(aggregate).size()-1).getVersion();
	}
}

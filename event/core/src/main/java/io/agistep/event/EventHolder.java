package io.agistep.event;


import java.util.List;

public interface EventHolder {

	static EventHolder instance() {
		return ThreadLocalEventHolder.instance();
	}

	void occurs(Event anEvent);

	List<Event> getEventAll();
	List<Event> getEvents(Object aggregate);

	void clearAll();

	void clear(Object aggregate);

	default boolean hasEventsOf(Object aggregate) {
		return !getEvents(aggregate).isEmpty();
	}

	default long getLatestVersionOf(Object aggregate) {
		return this.getEvents(aggregate).get(this.getEvents(aggregate).size()-1).getVersion();
	}
}

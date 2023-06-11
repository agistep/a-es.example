package io.agistep.event;


import java.util.List;

public interface EventList {

	static EventList instance() {
		return ThreadLocalEventList.instance();
	}

	List<Event> occurredListAll();

	void occurs(Event anEvent);

	List<Event> occurredListBy(Object aggregate);

	void publish();

}

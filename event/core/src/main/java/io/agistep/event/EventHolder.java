package io.agistep.event;


import io.agistep.aggregator.Aggregate;

import java.util.List;

public interface EventHolder {

	void hold(Event anEvent);

	List<Event> getEventAll();
	List<Event> getEvents(Aggregate aggregate);

	void clearAll();

	void clear(Aggregate aggregate);


}

package io.agistep.event;


import java.util.List;

public interface EventHolder {

	void hold(Event anEvent);

	List<Event> getEventAll();
	List<Event> getEvents(Object aggregate);

	void clearAll();

	void clear(Object aggregate);


}

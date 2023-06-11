package io.agistep.event;

public interface EventStore {
	void publish(Event anEvent);

}

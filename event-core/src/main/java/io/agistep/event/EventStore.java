package io.agistep.event;

import io.agistep.identity.Identity;

public interface EventStore {
	void appendToStream(Event anEvent);

}

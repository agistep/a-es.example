package io.agistep.event;

import io.agistep.identity.Identity;
import jdk.jfr.consumer.EventStream;

public interface EventStore {
	void appendToStream(Event anEvent);

	EventStream loadStreamOf(Identity anIdentity);
}

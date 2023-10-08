package io.agistep.event;

import java.time.LocalDateTime;

public interface Event {

	long getVersion();
	String getName();
	long getAggregateIdValue();
	Object getPayload();
	LocalDateTime getOccurredAt();

}

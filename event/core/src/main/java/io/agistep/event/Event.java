package io.agistep.event;

import java.time.LocalDateTime;

public interface Event {

	long getVersion();
	String getName();
	long getAggregateId();
	Object getPayload();
	LocalDateTime getOccurredAt();

}

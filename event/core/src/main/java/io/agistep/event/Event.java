package io.agistep.event;

import java.time.LocalDateTime;

public interface Event {

	long getId();

	long getSeq();
	String getName();
	long getAggregateId();
	Object getPayload();

	LocalDateTime getOccurredAt();

}

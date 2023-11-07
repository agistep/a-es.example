package io.agistep.event;

import io.agistep.aggregator.IdUtils;

import java.time.LocalDateTime;

final class EventApplier {

	static void apply(Object aggregate, Object payload) {

		final long eventId = IdUtils.gen();
		final long aggregateId;
		final long nextVersion;

		if(IdUtils.assertThatNotAssignIdOf(aggregate)) {
			aggregateId = IdUtils.gen();
			nextVersion = Events.BEGIN_VERSION;
		} else {
			aggregateId = IdUtils.idOf(aggregate);
			nextVersion = Events.nextVersion(aggregateId);
		}

		Event anEvent = Events.builder()
				.id(eventId)
				.aggregateId(aggregateId)
				.version(nextVersion)
				.name(payload.getClass().getName())
				.payload(payload)
				.occurredAt(LocalDateTime.now())
				.build();

		Events.hold(anEvent);
		//TODO reorganize 실패하면 hold 를 푼다.
		Events.reorganize(aggregate, anEvent);
	}

}

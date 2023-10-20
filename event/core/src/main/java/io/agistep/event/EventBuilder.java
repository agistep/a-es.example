package io.agistep.event;

import io.agistep.identity.IdentityValueProvider;

import java.time.LocalDateTime;

class EventBuilder {
	private String name;
	private long version;
	private long aggregateId;
	private Object payload;
	private LocalDateTime occurredAt;

	Event build() {
		return new ObjectPayloadEnvelop(
				name,
				version,
				aggregateId,
				payload,
				occurredAt);
	}

	EventBuilder name(String name) {
		this.name = name;
		return this;
	}

	@Deprecated
	EventBuilder version(long version) {
		this.version = version;
		return this;
	}

	@Deprecated
	EventBuilder aggregateIdValue(long aggregateIdValue) {
		this.aggregateId = aggregateIdValue;
		return this;
	}

	EventBuilder payload(Object payload) {
		this.payload = payload;
		return this;
	}

	EventBuilder occurredAt(LocalDateTime occurredAt) {
		this.occurredAt = occurredAt;
		return this;
	}

	public EventBuilder aggregate(Object aggregate) {
		this.version = ThreadLocalEventVersionHolder.instance().setVersion(aggregate);
		this.aggregateId = AggregateIdUtils.getIdFrom(aggregate) == -1 ?
				IdentityValueProvider.instance().newLong() : AggregateIdUtils.getIdFrom(aggregate);
		return this;
	}
}

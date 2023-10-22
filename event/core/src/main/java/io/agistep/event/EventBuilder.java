package io.agistep.event;

import io.agistep.identity.IdentityValueProvider;

import java.time.LocalDateTime;

public class EventBuilder {
	private long id;
	private String name;
	private long version;
	private long aggregateId;
	private Object payload;
	private LocalDateTime occurredAt;

	public Event build() {
		return new ObjectPayloadEnvelop(
				id,
				name,
				version,
				aggregateId,
				payload,
				occurredAt);
	}

	public EventBuilder id(long id) {
		this.id = id;
		return this;
	}

	public EventBuilder name(String name) {
		this.name = name;
		return this;
	}

	@Deprecated
	public EventBuilder version(long version) {
		this.version = version;
		return this;
	}

	@Deprecated
	public EventBuilder aggregateId(long aggregateIdValue) {
		this.aggregateId = aggregateIdValue;
		return this;
	}

	public EventBuilder payload(Object payload) {
		this.payload = payload;
		return this;
	}

	public EventBuilder occurredAt(LocalDateTime occurredAt) {
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

package io.agistep.event;

import java.time.LocalDateTime;

class EventBuilder {
	private String name;
	private long order;
	private long aggregateIdValue;
	private Object payload;
	private LocalDateTime occurredAt;

	Event build() {
		return new ObjectPayloadEnvelop(
				name,
				order,
				aggregateIdValue,
				payload,
				occurredAt);
	}

	EventBuilder name(String name) {
		this.name = name;
		return this;
	}

	EventBuilder order(long order) {
		this.order = order;
		return this;
	}

	EventBuilder aggregateIdValue(long aggregateIdValue) {
		this.aggregateIdValue = aggregateIdValue;
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
}

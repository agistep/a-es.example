package io.agistep.event;

import io.agistep.identity.IdentityValueProvider;

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

	@Deprecated
	EventBuilder order(long order) {
		this.order = order;
		return this;
	}

	@Deprecated
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

	public EventBuilder aggregate(Object aggregate) {
		this.order = ThreadLocalOrderMap.instance().setOrder(aggregate);
		//TODO aggregate 를 통해서 order 와 id를 가져올수있다 그러므로  aggregate builder 에 넘겨 처리하자
		this.aggregateIdValue = AggregateSupports.getId(aggregate) == -1 ?
				IdentityValueProvider.instance().newLong() : AggregateSupports.getId(aggregate);
		return this;
	}
}

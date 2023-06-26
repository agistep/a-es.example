package io.agistep.event;

import java.time.LocalDateTime;

public final class Events {

	public static final long BEGIN_ORDER = 1L;

	public static EventBuilder builder() {
		return new EventBuilder();
	}

	public static Event begin(long idValue, Object payload) {
		return Events.builder()
				.name(payload.getClass().getName())
				.order(BEGIN_ORDER)
				.aggregateIdValue(idValue)
				.payload(payload)
				.occurredAt(LocalDateTime.now())
				.build();
	}

	@Deprecated
	public static Event occurs(Object aggregate, Object payload) {
		final long aggregateIdValue = AggregateSupports.getId(aggregate);
		return occurs(aggregateIdValue, payload);
	}

	public static Event occurs(long aggregateIdValue, Object payload) {
		return Events.builder()
				.name(payload.getClass().getName())
				.order(-1) //TODO 이전 order 를 알아야한다.
				.aggregateIdValue(aggregateIdValue)
				.payload(payload)
				.occurredAt(LocalDateTime.now())
				.build();
	}

	public static Event mock(long aggregateIdValue, long order, Object payload) {
		return Events.builder()
				.name(payload.getClass().getName())
				.order(order) //TODO 이전 order 를 알아야한다.
				.aggregateIdValue(aggregateIdValue)
				.payload(payload)
				.occurredAt(LocalDateTime.now())
				.build();
	}


	public static class EventBuilder {
		private String name;
		private long order;
		private long aggregateIdValue;
		private Object payload;
		private LocalDateTime occurredAt;

		public Event build() {
			return new ObjectPayloadEnvelop(
					name,
					order,
					aggregateIdValue,
					payload,
					occurredAt);
		}

		public EventBuilder name(String name) {
			this.name = name;
			return this;
		}

		public EventBuilder order(long order) {
			this.order = order;
			return this;
		}

		public EventBuilder aggregateIdValue(long aggregateIdValue) {
			this.aggregateIdValue = aggregateIdValue;
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
	}
}

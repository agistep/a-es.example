package io.agistep.event;


import java.time.LocalDateTime;
import java.util.Objects;

class ObjectPayloadEnvelop implements Event {
	private final  String name;
	private final  long version;
	private final  long aggregateIdValue;
	private final  Object payload;
	private final  LocalDateTime occurredAt;


	ObjectPayloadEnvelop(String name, long version, long aggregateIdValue, Object payload, LocalDateTime occurredAt) {

		this.name = name;
		this.version = version;
		this.aggregateIdValue = aggregateIdValue;
		this.payload = payload;
		this.occurredAt = occurredAt;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public long getVersion() {
		return version;
	}

	@Override
	public long getAggregateId() {
		return aggregateIdValue;
	}

	@Override
	public Object getPayload() {
		return payload;
	}

	@Override
	public LocalDateTime getOccurredAt() {
		return occurredAt;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ObjectPayloadEnvelop that = (ObjectPayloadEnvelop) o;
		return version == that.version && aggregateIdValue == that.aggregateIdValue && Objects.equals(name, that.name) && Objects.equals(payload, that.payload) && Objects.equals(occurredAt, that.occurredAt);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, version, aggregateIdValue, payload, occurredAt);
	}
}

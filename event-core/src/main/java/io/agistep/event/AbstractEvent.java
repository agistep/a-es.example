package io.agistep.event;

import java.util.Objects;

public abstract class AbstractEvent implements Event{

	private final long aggregateIdValue;

	public AbstractEvent(long aggregateIdValue) {
		this.aggregateIdValue = aggregateIdValue;
	}

	@Override
	public long getAggregateIdValue() {
		return aggregateIdValue;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AbstractEvent that = (AbstractEvent) o;
		return aggregateIdValue == that.aggregateIdValue;
	}

	@Override
	public int hashCode() {
		return Objects.hash(aggregateIdValue);
	}
}

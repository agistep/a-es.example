package io.agistep.event;

public interface DomainEventApplier {
	static DomainEventApplier instance() {
		return SimpleDomainEventApplier.instance();
	}

	default void apply(Object aggregate, Event anEvent) {
		occurred(anEvent);
		replay(aggregate, anEvent);
	}

	void replay(Object aggregate, Event anEvent);

	void occurred(Event anEvent);
}

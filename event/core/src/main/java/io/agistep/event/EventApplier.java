package io.agistep.event;

public interface EventApplier {

	static EventApplier instance() {
		return EventApplierImpl.instance();
	}

	void apply(Object aggregate, Object payload);
}

package io.agistep.event;

public interface EventApplier {

	static EventApplier instance() {
		return EventApplierFacade.instance();
	}

	void apply(Object aggregate, Object payload);


}

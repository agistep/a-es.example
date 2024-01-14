package io.agistep.event;

import io.agistep.utils.BasePackageLoader;

final class EventReplayer {

	private static EventHandlerAdapterRetriever eventHandlerAdapterRetriever;

	static {
		var eventHandlerMethodLoader = new EventHandlerMethodAdapterLoader(new EventHandlerMethodScannerImpl(), new EventHandlerAdapterInitializerImpl());
		var adapters = eventHandlerMethodLoader.load(BasePackageLoader.load());

		eventHandlerAdapterRetriever = new EventHandlerAdapterRetrieverImpl(adapters);
	}

	static void replay(Object aggregate, Event anEvent) {
		validate(aggregate, anEvent);

		var payloadName = anEvent.getPayload().getClass().getName();
		EventHandlerMethodAdapter handler = eventHandlerAdapterRetriever.retrieve(payloadName);
		handler.handle(aggregate, anEvent);

		updateSeq(anEvent.getAggregateId(), anEvent.getSeq());
	}

	private static void validate(Object aggregate, Event anEvent) {
		if (anEvent == null) {
			throw new IllegalArgumentException("event should not be null");
		}
		if (aggregate == null) {
			throw new IllegalArgumentException("aggregate should not be null");
		}
	}

	private static void updateSeq(long aggregateId, long seq) {
		ThreadLocalEventSeqHolder.instance().setSeq(aggregateId, seq);
	}
}

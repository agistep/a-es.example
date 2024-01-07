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
		//TODO null empty
		var payloadName = anEvent.getPayload().getClass().getName();
		EventHandlerMethodAdapter handler = eventHandlerAdapterRetriever.retrieve(payloadName);
		handler.handle(aggregate, anEvent);

		updateSeq(anEvent.getAggregateId(), anEvent.getSeq());
	}

	private static void updateSeq(long aggregateId, long seq) {
		ThreadLocalEventSeqHolder.instance().setSeq(aggregateId, seq);
	}
}

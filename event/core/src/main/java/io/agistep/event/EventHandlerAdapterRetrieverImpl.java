package io.agistep.event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventHandlerAdapterRetrieverImpl implements EventHandlerAdapterRetriever {

    private final Map<String, EventHandlerMethodAdapter> payloadToHandlerAdapter;

    public EventHandlerAdapterRetrieverImpl(List<EventHandlerMethodAdapter> eventHandlerMethodAdapters) {
        payloadToHandlerAdapter = new HashMap<>();
        eventHandlerMethodAdapters.forEach(
                handlerAdapter -> payloadToHandlerAdapter.put(handlerAdapter.getPayloadName(), handlerAdapter)
        );
    }

    @Override
    public EventHandlerMethodAdapter retrieve(String payloadName) {

        return payloadToHandlerAdapter.get(payloadName);
    }
}

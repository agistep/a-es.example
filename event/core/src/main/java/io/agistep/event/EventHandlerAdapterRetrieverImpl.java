package io.agistep.event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventHandlerAdapterRetrieverImpl implements EventHandlerAdapterRetriever {

    private final Map<String, EventHandlerMethodAdapter> handlers;


    public EventHandlerAdapterRetrieverImpl(List<EventHandlerMethodAdapter> eventHandlerMethodAdapters) {
        handlers = new HashMap<>();
        eventHandlerMethodAdapters.forEach(handlerAdapter -> handlers.put(handlerAdapter.getPayloadName(), handlerAdapter));
    }


    @Override
    public EventHandlerMethodAdapter retrieve(String payloadName) {

        return handlers.get(payloadName);
    }
}

package io.agistep.event;

interface EventHandlerAdapterRetriever {
    EventHandlerMethodAdapter retrieve(String payloadName);
}

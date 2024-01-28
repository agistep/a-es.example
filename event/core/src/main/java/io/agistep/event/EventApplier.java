package io.agistep.event;

import io.agistep.aggregator.IdUtils;

import java.time.LocalDateTime;
import java.util.Optional;

final class EventApplier {

    static void apply(Object aggregate, Object payload) {

        final Event anEvent = make(aggregate, payload);
        EventMaker.make();
        //TODO replay 실패하면 hold 를 푼다.
        replay(aggregate, anEvent);
        hold(anEvent);
    }

    private static Event make(Object aggregate, Object payload) {
        final long eventId = IdUtils.gen();
        final long aggregateId;
        final long nextSeq;

        if (IdUtils.notAssignedIdOf(aggregate)) {
            aggregateId = IdUtils.gen();
            nextSeq = EventSource.INITIAL_SEQ;
        } else {
            aggregateId = IdUtils.idOf(aggregate);
            nextSeq = nextSeq(aggregateId);
        }

        return EventSource.builder()
                .id(eventId)
                .aggregateId(aggregateId)
                .seq(nextSeq)
                .payload(payload)
                .occurredAt(LocalDateTime.now())
                .build();
    }

    private static void hold(Event anEvent) {
        Optional.ofNullable(EventSource.holdListener).ifPresent(listen->listen.beforeHold(anEvent));
        ThreadLocalEventHolder.instance().hold(anEvent);
        Optional.ofNullable(EventSource.holdListener).ifPresent(listen->listen.afterHold(anEvent));
    }

    private static void replay(Object aggregate, Event anEvent) {
        Optional.ofNullable(EventSource.replayListener).ifPresent (listen-> listen.beforeReplay(aggregate, anEvent));
        EventReplayer.replay(aggregate, anEvent);
        Optional.ofNullable(EventSource.replayListener).ifPresent (listen-> listen.afterReplay(aggregate, anEvent));
    }

    private static long nextSeq(Object aggregateId) {
        return ThreadLocalEventSeqHolder.instance().nextSeq((Long) aggregateId);
    }

    final static ReplayListener DUMMY = new ReplayListener() {
        @Override
        public void beforeReplay(Object aggregate, Event event) {

        }

        @Override
        public void afterReplay(Object aggregate, Event event) {

        }
    };

}

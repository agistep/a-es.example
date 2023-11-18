package io.agistep.event;

import io.agistep.aggregator.IdUtils;

import java.time.LocalDateTime;
import java.util.Optional;

final class EventApplier {

    static void apply(Object aggregate, Object payload) {

        final Event anEvent = make(aggregate, payload);

        //TODO reorganize 실패하면 hold 를 푼다.
        reorganize(aggregate, anEvent);
        hold(anEvent);
    }

    private static Event make(Object aggregate, Object payload) {
        final long eventId = IdUtils.gen();
        final long aggregateId;
        final long nextSeq;

        if (IdUtils.notAssignedIdOf(aggregate)) {
            aggregateId = IdUtils.gen();
            nextSeq = Events.INITIAL_SEQ;
        } else {
            aggregateId = IdUtils.idOf(aggregate);
            nextSeq = nextSeq(aggregateId);
        }

        return Events.builder()
                .id(eventId)
                .aggregateId(aggregateId)
                .seq(nextSeq)
                //TODO payload 가 string 같은 놈이라면 ???
                .name(payload.getClass().getName())
                .payload(payload)
                .occurredAt(LocalDateTime.now())
                .build();
    }

    private static void hold(Event anEvent) {
        Optional.ofNullable(Events.holdListener).ifPresent(listen->listen.beforeHold(anEvent));
        ThreadLocalEventHolder.instance().hold(anEvent);
        Optional.ofNullable(Events.holdListener).ifPresent(listen->listen.afterHold(anEvent));
    }

    private static void reorganize(Object aggregate, Event anEvent) {
        Optional.ofNullable(Events.reorganizeListener).ifPresent (listen-> listen.beforeReorganize(aggregate, anEvent));
        EventReorganizer.reorganize(aggregate, anEvent);
        Optional.ofNullable(Events.reorganizeListener).ifPresent (listen-> listen.afterReorganize(aggregate, anEvent));
    }

    private static long nextSeq(Object aggregateId) {
        return ThreadLocalEventSeqHolder.instance().nextSeq((Long) aggregateId);
    }

    final static ReorganizeListener DUMMY = new ReorganizeListener() {
        @Override
        public void beforeReorganize(Object aggregate, Event event) {

        }

        @Override
        public void afterReorganize(Object aggregate, Event event) {

        }
    };

}

package io.agistep.event;

import io.agistep.aggregator.ConfigIdGenerator;
import io.agistep.aggregator.IdUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static io.agistep.event.EventMaker.make;

final class EventApplier {

    static void apply(Object aggregate, Object payload) {

        final Event anEvent = make(aggregate, payload);
//        EventMaker.make();
        //TODO replay 실패하면 hold 를 푼다.
        replay(aggregate, anEvent);
        hold(anEvent);
    }
    static ConfigIdGenerator generator = new ConfigIdGenerator();

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

    final static ReplayListener DUMMY = new ReplayListener() {
        @Override
        public void beforeReplay(Object aggregate, Event event) {

        }

        @Override
        public void afterReplay(Object aggregate, Event event) {

        }
    };

}

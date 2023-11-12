package io.agistep.event.test;

import io.agistep.aggregator.IdUtils;
import io.agistep.event.Event;
import io.agistep.event.Events;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static io.agistep.event.test.EventFixtureBuilder.eventsWith;
import static io.agistep.event.test.EventMatchConditions.*;
import static org.assertj.core.api.Assertions.assertThat;

public final class EventSourcingAssertions {

    public static <AGG> ABC<AGG> assertEventSourcing(Supplier<AGG> initAggregate) {
        return new ABC<>(initAggregate);
    }

    public static class ABC<AGG> {
        private final Supplier<AGG> initAggregate;

        ABC(Supplier<AGG> initAggregate) {
            this.initAggregate = initAggregate;
        }

        public DEF<AGG> given() {
            return given(new Event[0]);
        }

        public DEF<AGG> given(Event ... recently) {
            return new DEF<>(initAggregate, recently);
        }
    }

    public static class DEF<AGG> {
        private final Supplier<AGG> initAggregate;
        private final Event[] recently;

        DEF(Supplier<AGG> initAggregate, Event[] recently) {
            this.initAggregate = initAggregate;
            this.recently = recently;
        }

        public HIG<AGG> when(Consumer<AGG> aggregateProcessor) {
            return new HIG<>(this, aggregateProcessor);
        }

    }

    public static class HIG<AGG> {

        private final DEF<AGG> def;
        private final Consumer<AGG> aggregateProcessor;

        public HIG(DEF<AGG> def, Consumer<AGG> aggregateProcessor) {
            this.def = def;
            this.aggregateProcessor = aggregateProcessor;
        }

        public void expected(Object ... payloads) {
            testEventSourcing(
                    def.initAggregate,
                    def.recently,
                    aggregateProcessor,
                    payloads);
        }

        private void testEventSourcing(Supplier<AGG> initAggregate, Event[] recently, Consumer<AGG> aggregateProcessor, Object[] expected) {
            Pair<AGG> pair = getPair(recently, initAggregate);
            aggregateProcessor.accept(pair.aggregate);
            abc(pair.aggregate, pair.latestSeq, expected); // created 와 done 이 잘 발생했는가?
        }

        private Pair<AGG> getPair(Event[] recently, Supplier<AGG> initAggregate) {
            AGG aggregate = initAggregate.get();
            Events.reorganize(aggregate, recently);
            final long latestSeq = getLatestSeq(aggregate);

            return new Pair<>(aggregate, latestSeq);
        }

        private static long getLatestSeq(Object aggregate) {
            long seq;
            try {
                seq = Events.getLatestSeqOf(aggregate);
            } catch (Exception e) {
                seq = -1;
            }
            return seq;
        }

        private void abc(Object aggregate, long seq, Object... expectedPayload) {
            final long aggregateId = IdUtils.idOf(aggregate);

            Event[] expected = getExpected(aggregateId, seq, expectedPayload);
            Event[] actual = getActual();

            assertThat(actual.length).isEqualTo(expected.length);

            for (int i = 0; i < expected.length; i++) {
                //assertThat(actual[i]).is(idCondition(expected[i]));
                assertThat(actual[i]).is(aggregateIdCondition(expected[i]));
                assertThat(actual[i]).is(seqCondition(expected[i]));
                assertThat(actual[i]).is(nameCondition(expected[i]));
                assertThat(actual[i]).is(payloadCondition(expected[i]));
                assertThat(actual[i]).is(occurredAtCondition(expected[i]));
            }
        }

        private Event[] getActual() {
            return HoldingEventLogger.init().getAll();
        }

        private static Event[] getExpected(long aggregateId, long seq, Object[] expectedPayload) {
            if (expectedPayload == null || expectedPayload.length == 0) {
                return new Event[0];
            }
            EventFixtureBuilder eventsWith = eventsWith(aggregateId, expectedPayload[0]);

            for (int i = 1; i < expectedPayload.length; ++i) {
                eventsWith.next(expectedPayload[i]);
            }
            return eventsWith.build(seq);
        }

    }

    private static class Pair<AGG> {
        final AGG aggregate;
        final long latestSeq;

        Pair(AGG aggregate, long latestSeq) {
            this.aggregate = aggregate;
            this.latestSeq = latestSeq;
        }
    }
}

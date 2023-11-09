package io.agistep.event.test;

import io.agistep.event.Event;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static io.agistep.event.Events.BEGIN_VERSION;
import static io.agistep.event.test.EventListBuilder.forTestWith;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EventListBuilderTest {

    public static final Created FIRST_PAYLOAD = new Created();
    public static final Done SECOND_PAYLOAD = new Done();
    public static final Object NULL_PAYLOAD = null;

    static class Created {}
    static class Done {}

    @Test
    @DisplayName("Exception: payload must be not null")
    void forTestWith0() {
        assertThatThrownBy(()->forTestWith(NULL_PAYLOAD));
    }

    @Test
    @DisplayName("Exception: payload must be not null")
    void forTestWith00() {
        assertThatThrownBy(()->
                forTestWith(aggregate(1L), FIRST_PAYLOAD)
                .next(NULL_PAYLOAD));
    }

    @Test
    void forTestWith1() {

        Event[] actual = forTestWith(FIRST_PAYLOAD)
                .next(SECOND_PAYLOAD)
                .build();

        //assertThat(actual[0].getId()).isEqualTo(???)
        assertThat(actual[0].getAggregateId()).isEqualTo(actual[1].getAggregateId());

        assertThat(actual[0].getVersion()).isEqualTo(BEGIN_VERSION);
        assertThat(actual[0].getName()).isEqualTo(FIRST_PAYLOAD.getClass().getName());
        assertThat(actual[0].getPayload()).isEqualTo(FIRST_PAYLOAD);
        assertThat(actual[0].getOccurredAt()).isEqualToIgnoringNanos(LocalDateTime.now());

        assertThat(actual[1].getVersion()).isEqualTo(actual[0].getVersion()+1);
        assertThat(actual[1].getName()).isEqualTo(SECOND_PAYLOAD.getClass().getName());
        assertThat(actual[1].getPayload()).isEqualTo(SECOND_PAYLOAD);
        assertThat(actual[1].getOccurredAt()).isEqualToIgnoringNanos(LocalDateTime.now());



    }

    private long aggregate(long id) {
        return id;
    }
}
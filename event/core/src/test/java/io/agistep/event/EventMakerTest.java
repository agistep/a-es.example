package io.agistep.event;

import io.agistep.foo.Foo;
import io.agistep.foo.FooCreated;
import io.agistep.foo.FooDone;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class EventMakerTest extends EventApplySupport {

    @Test
    @DisplayName("Exception: make event with invalid event name")
    void eventName() {
        FooCreated payload = new FooCreated();
        assertThatThrownBy(() -> EventMaker.make(
                1L,
                1L,
                0L,
                "invalid_event_name",
                LocalDateTime.of(2023, 12, 12, 0, 0),
                payload
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Event name should be same with payload class name.");
    }


    @Test
    @DisplayName("make event")
    void makeEvent() {
        Foo aggregate = new Foo();

        FooCreated fooCreated = new FooCreated();
        String createdEventName = fooCreated.getClass().getName();
        EventSource.apply(aggregate, fooCreated);

        assertThat(aggregate.isDone()).isFalse();
        assertThat(EventSource.getHoldEvents(aggregate).get(0).getName()).isEqualTo(createdEventName);
        assertThat(EventSource.getLatestSeqOf(aggregate)).isEqualTo(EventSource.INITIAL_SEQ);

        FooDone fooDone = new FooDone();
        String doneEventName = fooDone.getClass().getName();
        EventSource.apply(aggregate, fooDone);

        assertThat(aggregate.isDone()).isTrue();
        assertThat(EventSource.getHoldEvents(aggregate)).hasSize(2);
        assertThat(EventSource.getHoldEvents(aggregate).get(1).getName()).isEqualTo(doneEventName);
        assertThat(EventSource.getLatestSeqOf(aggregate)).isEqualTo(EventSource.INITIAL_SEQ +1);
    }
}

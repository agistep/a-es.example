package io.agistep.todo.domain;

import io.agistep.event.EventSource;
import io.agistep.event.test.HoldingEventListener;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.agistep.event.test.EventSourcingAssertions.assertEventSourcing;

class TodoHeldEventTest {

    @BeforeEach
    void setUp() {
        EventSource.setListener(new HoldingEventListener());
    }

    @AfterEach
    void tearDown() {
        EventSource.setListener(null);
    }

    @Test
    void hold() {
        assertEventSourcing(Todo::new)
                .given(TodoCreated.newBuilder().setText("Some Text").build())

				.when(Todo::hold)

				.expected(TodoHeld.newBuilder().build())
                .extracting("hold").isEqualTo(true);
    }

    @Test
    void skipHold() {
		assertEventSourcing(Todo::new)
				.given(TodoCreated.newBuilder().setText("Some Text").build(),
						TodoDone.newBuilder().build())

				.when(Todo::hold)

				.expected()
				.extracting("hold").isEqualTo(false);

    }
}

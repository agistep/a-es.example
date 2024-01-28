package io.agistep.todo.domain;

import org.junit.jupiter.api.Test;

import static io.agistep.event.test.EventSourcingAssertions.assertEventSourcing;

class TodoHeldEventTest extends EventApplySupport {

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

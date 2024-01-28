package io.agistep.todo.domain;

import org.junit.jupiter.api.Test;

import static io.agistep.event.test.EventSourcingAssertions.assertEventSourcing;

class TodoCreatedEventTest extends EventApplySupport {


    @Test
    void created() {
		assertEventSourcing(() -> new Todo("Some Text"))
				.given()

				.when((aggregate) -> {
				})

				.expected(TodoCreated.newBuilder().setText("Some Text").build())

				.extracting("text").isEqualTo("Some Text");

	}

}

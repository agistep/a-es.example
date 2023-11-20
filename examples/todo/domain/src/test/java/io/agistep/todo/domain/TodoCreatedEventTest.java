package io.agistep.todo.domain;

import io.agistep.event.EventSource;
import io.agistep.event.test.HoldingEventListener;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.agistep.event.test.EventSourcingAssertions.assertEventSourcing;

class TodoCreatedEventTest {

    @BeforeEach
    void setUp() {
        EventSource.setListener(new HoldingEventListener());
    }

    @AfterEach
    void tearDown() {
        EventSource.setListener(null);
    }

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
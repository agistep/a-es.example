package io.agistep.todo.domain;

import io.agistep.event.EventSource;
import io.agistep.event.test.HoldingEventListener;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.agistep.event.test.EventSourcingAssertions.assertEventSourcing;

class TodoUpdatedEventTest {


    @BeforeEach
    void setUp() {
        EventSource.setListener(new HoldingEventListener());
    }

    @AfterEach
    void tearDown() {
        EventSource.setListener(null);
    }

    @Test
    void done() {
        TodoCreated created = TodoCreated.newBuilder().setText("Some Text").build();

        String updatedText = "Updated Text";
        assertEventSourcing(Todo::new)
                .given(created)

                .when(e -> e.updateText(updatedText))

                .expected(TodoTextUpdated.newBuilder().setUpdatedText(updatedText).build())

				.extracting("text").isEqualTo(updatedText);
    }


}

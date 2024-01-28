package io.agistep.todo.domain;

import org.junit.jupiter.api.Test;

import static io.agistep.event.test.EventSourcingAssertions.assertEventSourcing;

class TodoUpdatedEventTest extends EventApplySupport {

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

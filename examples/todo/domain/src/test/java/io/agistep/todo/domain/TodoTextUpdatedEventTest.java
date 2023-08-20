package io.agistep.todo.domain;

import io.agistep.event.TestEvents;
import org.junit.jupiter.api.Test;

import static io.agistep.event.EventAssertions.assertThatOccurredExactlyOnes;

class TodoTextUpdatedEventTest {

	@Test
	void textChangedEvent() {
		Object[] payload = new Object[]{TodoCreated.newBuilder().setText("Some Text").build()};
		Todo sut = Todo.reorganize(TestEvents.events(1L, payload));

		sut.updateText("Updated Text");

		assertThatOccurredExactlyOnes(sut, TodoTextUpdated.newBuilder().setUpdatedText("Updated Text").build());
	}


}

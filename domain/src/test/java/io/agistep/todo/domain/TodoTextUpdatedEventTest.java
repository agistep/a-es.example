package io.agistep.todo.domain;

import io.agistep.event.Events;
import org.junit.jupiter.api.Test;

import static io.agistep.event.EventAssertions.assertThatOccurredExactlyOnes;

class TodoTextUpdatedEventTest {

	@Test
	void textChangedEvent() {
		Todo sut = Todo.replay(
				Events.begin(new TodoCreated("Some Text")));

		sut.updateText("Updated Text");

		assertThatOccurredExactlyOnes(sut, Events.occurs(sut, new TodoTextUpdated("Updated Text")));
	}


}

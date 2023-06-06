package io.agistep.todo.domain;

import org.junit.jupiter.api.Test;

import static io.agistep.event.EventAssertions.assertThatOccurredExactly;

class TodoTextUpdatedEventTest {

	@Test
	void textChangedEvent() {
		Todo sut = Todo.replay(new TodoCreated(999L, "Some text"));

		sut.updateText("Updated Text");

		assertThatOccurredExactly(sut, new TodoTextUpdated(sut.getId().getValue(), "Updated Text") );
	}


}

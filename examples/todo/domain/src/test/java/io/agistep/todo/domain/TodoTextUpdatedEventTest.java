package io.agistep.todo.domain;

import io.agistep.event.Event;
import io.agistep.event.EventReorganizor;
import io.agistep.event.TestEvents;
import org.junit.jupiter.api.Test;

import static io.agistep.event.EventAssertions.assertThatOccurredExactlyOnes;

class TodoTextUpdatedEventTest {

	@Test
	void textChangedEvent() {
		Event[] events = TestEvents.anEvent(1L, TodoCreated.newBuilder().setText("Some Text").build());
		Todo sut = new Todo();
		EventReorganizor.reorganize(sut, events);

		sut.updateText("Updated Text");

		assertThatOccurredExactlyOnes(sut, TodoTextUpdated.newBuilder().setUpdatedText("Updated Text").build());
	}


}

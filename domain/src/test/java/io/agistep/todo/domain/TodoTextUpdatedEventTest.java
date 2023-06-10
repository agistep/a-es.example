package io.agistep.todo.domain;

import io.agistep.event.Events;
import org.junit.jupiter.api.Test;

import static io.agistep.event.EventAssertions.assertThatOccurredExactlyOnes;

class TodoTextUpdatedEventTest {

	@Test
	void textChangedEvent() {
		Todo sut = Todo.replay(
				/*TODO 여기보자... 왜 begin 을 사용하는가? begin 을 사용하것을 왜 문제 시 삼으려 하는가?*/
				Events.begin(TodoCreated.newBuilder().setText("Some Text").build()));

		sut.updateText("Updated Text");

		assertThatOccurredExactlyOnes(sut, Events.occurs(sut, TodoTextUpdated.newBuilder().setUpdatedText("Updated Text").build()));
	}


}

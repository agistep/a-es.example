package io.agistep.todo.domain;

import io.agistep.event.Events;
import org.junit.jupiter.api.Test;

import static io.agistep.event.EventAssertions.assertThatOccurredExactlyOnes;
import static org.assertj.core.api.Assertions.assertThat;

class TodoCreatedEventTest {

	Todo sut = new Todo("Some Text");

	@Test
	void created() {
		assertThatOccurredExactlyOnes(sut, Events.mock(sut.getId().getValue(), 1, new TodoCreated("Some Text")));
	}

	@Test
	void properties() {
		assertThat(sut.getText()).isEqualTo("Some Text");
		assertThat(sut.isDone()).isEqualTo(false);
	}
}
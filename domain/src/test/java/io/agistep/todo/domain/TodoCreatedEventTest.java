package io.agistep.todo.domain;

import org.junit.jupiter.api.Test;

import static io.agistep.event.EventAssertions.assertThatOccurredExactly;
import static org.assertj.core.api.Assertions.assertThat;

class TodoCreatedEventTest {

	Todo sut = new Todo("Some Text");

	@Test
	void created() {
		assertThatOccurredExactly(sut, new TodoCreated(sut.getId().getValue(), "Some Text"));
	}

	@Test
	void properties() {
		assertThat(sut.getText()).isEqualTo("Some Text");
		assertThat(sut.isDone()).isEqualTo(false);
	}
}
package io.agistep.todo.domain;

import org.junit.jupiter.api.Test;

import static io.agistep.event.EventAssertions.assertThatOccurredExactly;
import static org.assertj.core.api.Assertions.assertThat;

class TodoDoneEventTest {

	@Test
	void done() {
		Todo sut = Todo.replay(new TodoCreated(99L, "Some Text"));

		sut.done();

		assertThatOccurredExactly(sut, new TodoDone(sut.getId().getValue()));
	}

	@Test
	void properties() {
		Todo sut = Todo.replay(new TodoCreated(100L, "Some Text"));
		assertThat(sut.isDone()).isFalse();

		sut.done();
		assertThat(sut.isDone()).isTrue();
	}
}
package io.agistep.todo.domain;

import io.agistep.event.Events;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.agistep.event.EventAssertions.assertThatOccurredExactlyOnes;
import static org.assertj.core.api.Assertions.assertThat;

class TodoDoneEventTest {

	Todo sut;

	@BeforeEach
	void setUp() {
		sut = Todo.replay(Events.mock(1919, 1, new TodoCreated("Some Text")));
	}

	@Test
	void done() {
		sut.done();

		assertThatOccurredExactlyOnes(sut, Events.mock(1919, -1, new TodoDone()));
	}

	@Test
	void properties() {
		assertThat(sut.isDone()).isFalse();

		sut.done();
		assertThat(sut.isDone()).isTrue();
	}
}
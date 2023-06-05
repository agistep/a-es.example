package io.agistep.todo.domain;

import org.junit.jupiter.api.Test;

import static io.agistep.event.EventAssertions.assertThatDoesNotOccurAnEventBy;
import static org.assertj.core.api.Assertions.assertThat;

class TodoHeldEvenTest {


	@Test
	void hold() {
		Todo sut = new Todo(new TodoCreated(99999L, "Some Text"), new TodoDone(99999L));
		assertThat(sut.isDone()).isTrue();

		sut.hold();

		assertThatDoesNotOccurAnEventBy(sut);
	}
}

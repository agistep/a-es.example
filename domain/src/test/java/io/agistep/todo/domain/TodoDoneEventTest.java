package io.agistep.todo.domain;

import io.agistep.event.EventList;
import io.agistep.event.TestEvents;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.agistep.event.EventAssertions.assertThatOccurredExactlyOnes;
import static org.assertj.core.api.Assertions.assertThat;

class TodoDoneEventTest {

	Todo sut;

	@BeforeEach
	void setUp() {
		EventList.instance().publish();
		TodoCreated created = TodoCreated.newBuilder().setText("Some Text").build();
		sut = Todo.reorganize(TestEvents.events(1919, new Object[]{created}));
	}

	@Test
	void done() {
		sut.done();
		assertThatOccurredExactlyOnes(sut, TodoDone.newBuilder().build());
	}

	@Test
	void properties() {
		assertThat(sut.isDone()).isFalse();
		sut.done();
		assertThat(sut.isDone()).isTrue();
	}
}
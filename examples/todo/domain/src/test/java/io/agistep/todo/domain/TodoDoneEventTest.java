package io.agistep.todo.domain;

import io.agistep.event.EventHolder;
import io.agistep.event.TestEvents;
import io.agistep.event.ThreadLocalEventVersionHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.agistep.event.EventAssertions.assertThatOccurredExactlyOnes;
import static org.assertj.core.api.Assertions.assertThat;

class TodoDoneEventTest {

	Todo sut;

	@BeforeEach
	void setUp() {
		EventHolder.instance().clearAll();

		TodoCreated created = TodoCreated.newBuilder().setText("Some Text").build();
		sut = Todo.reorganize(TestEvents.anEvent(1919, created));

		//assertThat(EventList.instance().getLatestOrderOf(sut)).isEqualTo(1);
	}

	@Test
	void done() {
		sut.done();
		assertThat(EventHolder.instance().getLatestVersionOf(sut)).isEqualTo(1);
		assertThatOccurredExactlyOnes(sut, TodoDone.newBuilder().build());
	}

	@Test
	void properties() {
		assertThat(sut.isDone()).isFalse();
		sut.done();
		assertThat(sut.isDone()).isTrue();
	}
}
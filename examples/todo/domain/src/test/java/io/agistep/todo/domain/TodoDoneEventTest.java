package io.agistep.todo.domain;

import io.agistep.event.EventList;
import io.agistep.event.TestEvents;
import io.agistep.event.ThreadLocalEventVersionMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.agistep.event.EventAssertions.assertThatOccurredExactlyOnes;
import static org.assertj.core.api.Assertions.assertThat;

class TodoDoneEventTest {

	Todo sut;

	@BeforeEach
	void setUp() {
		EventList.instance().clear();
		ThreadLocalEventVersionMap.instance().clear();

		TodoCreated created = TodoCreated.newBuilder().setText("Some Text").build();
		sut = Todo.reorganize(TestEvents.events(1919, created));

		//assertThat(EventList.instance().getLatestOrderOf(sut)).isEqualTo(1);
	}

	@Test
	void done() {
		sut.done();
		assertThat(EventList.instance().getLatestVersionOf(sut)).isEqualTo(1);
		assertThatOccurredExactlyOnes(sut, TodoDone.newBuilder().build());
	}

	@Test
	void properties() {
		assertThat(sut.isDone()).isFalse();
		sut.done();
		assertThat(sut.isDone()).isTrue();
	}
}
package io.agistep.todo.domain;

import io.agistep.event.Event;
import io.agistep.event.TestEvents;
import org.junit.jupiter.api.Test;

import static io.agistep.event.EventAssertions.assertThatDoesNotOccurAnEventBy;
import static org.assertj.core.api.Assertions.assertThat;

class TodoHeldEvenTest {


	@Test
	void hold() {
		TodoCreated created = TodoCreated.newBuilder().setText("Some Text").build();
		TodoDone done = TodoDone.newBuilder().build();
		int aggregateIdValue = 99;
		Event[] events = TestEvents.events(aggregateIdValue, created, done);

		Todo sut = Todo.reorganize(events);

		assertThat(sut.isDone()).isTrue();

		sut.hold();

		assertThatDoesNotOccurAnEventBy(sut);
	}

}

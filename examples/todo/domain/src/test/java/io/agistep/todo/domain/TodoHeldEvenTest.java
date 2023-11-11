package io.agistep.todo.domain;

import io.agistep.event.Event;
import io.agistep.event.Events;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TodoHeldEvenTest {


	Todo sut;

	@BeforeEach
	void setUp() {
		Events.clearAll();
		TodoCreated created = TodoCreated.newBuilder().setText("Some Text").build();
		Event anEvent1 = Events.builder()
				.id(1L)
				.version(0L)

				.aggregateId(1L)

				.name(created.getClass().getName())
				.payload(created)
				.occurredAt(LocalDateTime.now())
				.build();

		this.sut = new Todo();
		Events.reorganize(sut, new Event[]{anEvent1});
	}

	@Test
	void hold() {

		sut.hold();

		List<Event> actual = Events.getHoldEvents(sut);
		assertThat(actual).hasSize(1);
		assertThat(Events.getLatestVersionOf(sut.getId())).isEqualTo(1);
		assertThat(actual.get(0).getAggregateId()).isEqualTo(sut.getId());
		assertThat(actual.get(0).getVersion()).isEqualTo(Events.getLatestVersionOf(sut.getId()));
		assertThat(actual.get(0).getName()).isEqualTo(TodoHeld.class.getName());
		assertThat(actual.get(0).getPayload()).isInstanceOf(TodoHeld.class);
	}

	@Test
	void skipHold() {
		TodoDone done = TodoDone.newBuilder().build();
		Event anEvent2 = Events.builder()
				.id(2L)
				.version(1L)

				.aggregateId(1L)

				.name(done.getClass().getName())
				.payload(done)
				.occurredAt(LocalDateTime.now())
				.build();

		Events.reorganize(sut, new Event[]{anEvent2});

		sut.hold();

		assertThat(Events.getHoldEvents(sut)).isEmpty();

	}
}

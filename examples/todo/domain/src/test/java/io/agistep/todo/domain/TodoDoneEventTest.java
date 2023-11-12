package io.agistep.todo.domain;

import io.agistep.event.Event;
import io.agistep.event.Events;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static io.agistep.event.test.EventFixtureBuilder.anEventWith;
import static org.assertj.core.api.Assertions.assertThat;

class TodoDoneEventTest {

	Todo sut;

	@BeforeEach
	void setUp() {
		Events.clearAll();

		sut = new Todo();
		TodoCreated created = TodoCreated.newBuilder().setText("Some Text").build();
		Events.reorganize(sut, new Event[]{anEventWith(created)});
	}

	@Test
	void done() {
		sut.done();

		List<Event> actual = Events.getHoldEvents(sut);
		assertThat(actual).hasSize(1);
		assertThat(Events.getLatestSeqOf(sut.getId())).isEqualTo(Events.INITIAL_SEQ +1);
		assertThat(actual.get(0).getAggregateId()).isEqualTo(sut.getId());
		assertThat(actual.get(0).getSeq()).isEqualTo(Events.INITIAL_SEQ +1);
		assertThat(actual.get(0).getName()).isEqualTo(TodoDone.class.getName());
		assertThat(actual.get(0).getPayload()).isInstanceOf(TodoDone.class);
	}

	@Test
	void properties() {
		assertThat(sut.isDone()).isFalse();

		sut.done();

		assertThat(sut.isDone()).isTrue();
	}
}
package io.agistep.todo.domain;

import io.agistep.event.Event;
import io.agistep.event.Events;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TodoCreatedEventTest {

	Todo sut;

	@BeforeEach
	void setUp() {
		Events.clearAll();
	}

	@Test
	void created() {
		sut = new Todo("Some Text");

		List<Event> actual = Events.getHoldEvents(sut);
		assertThat(actual).hasSize(1);
		assertThat(Events.getLatestVersionOf(sut.getId())).isEqualTo(Events.BEGIN_VERSION);
		assertThat(actual.get(0).getAggregateId()).isEqualTo(sut.getId());
		assertThat(actual.get(0).getVersion()).isEqualTo(Events.BEGIN_VERSION);
		assertThat(actual.get(0).getName()).isEqualTo(TodoCreated.class.getName());
		assertThat(actual.get(0).getPayload()).isInstanceOf(TodoCreated.class);
		assertThat(actual.get(0).getPayload()).extracting("text").isEqualTo("Some Text");


	}

}
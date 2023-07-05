package io.agistep.todo.domain;

import io.agistep.event.EventList;
import io.agistep.event.Events;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.agistep.event.EventAssertions.assertThatOccurredExactlyOnes;
import static org.assertj.core.api.Assertions.assertThat;

class TodoCreatedEventTest {

	Todo sut;

	@BeforeEach
	void setUp() {
		EventList.instance().publish();
		sut = new Todo("Some Text");
	}

	@Test
	void created() {
		assertThatOccurredExactlyOnes(sut, Events.mock(sut.getId().getValue(), TodoCreated.newBuilder().setText("Some Text").build()));
	}

	@Test
	void properties() {
		assertThat(sut.getText()).isEqualTo("Some Text");
		assertThat(sut.isDone()).isEqualTo(false);
	}
}
package io.agistep.todo.domain;

import io.agistep.event.EventList;
import io.agistep.event.TestEvents;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.agistep.event.EventAssertions.assertThatOccurredExactlyOnes;
import static org.assertj.core.api.Assertions.assertThat;

class TodoEventOrderTest {
	Todo sut;

	@BeforeEach
	void setUp() {
		EventList.instance().clear();
		sut = new Todo("Some Text");

		assertThat(EventList.instance().getLatestOrderOf(sut)).isEqualTo(1);
	}

	@Test
	void done() {
		assertThat(EventList.instance().getLatestOrderOf(sut)).isEqualTo(1);

		sut.done();

		assertThat(EventList.instance().getLatestOrderOf(sut)).isEqualTo(2);
	}


}

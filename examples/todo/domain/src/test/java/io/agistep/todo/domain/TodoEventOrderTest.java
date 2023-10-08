package io.agistep.todo.domain;

import io.agistep.event.EventList;
import io.agistep.event.ThreadLocalEventVersionMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.agistep.event.EventAssertions.assertThatOccurredExactlyOnes;
import static org.assertj.core.api.Assertions.assertThat;

class TodoEventOrderTest {
	Todo sut;

	@BeforeEach
	void setUp() {
		EventList.instance().clear();
		ThreadLocalEventVersionMap.instance().clear();

		sut = new Todo("Some Text");

		assertThat(EventList.instance().getLatestVersionOf(sut)).isEqualTo(0);
	}

	@Test
	void done() {
		assertThat(EventList.instance().getLatestVersionOf(sut)).isEqualTo(0);

		sut.done();

		assertThat(EventList.instance().getLatestVersionOf(sut)).isEqualTo(1);
	}


}

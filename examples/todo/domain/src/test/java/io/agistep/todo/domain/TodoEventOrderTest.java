package io.agistep.todo.domain;

import io.agistep.event.EventHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TodoEventOrderTest {
	Todo sut;

	@BeforeEach
	void setUp() {
		EventHolder.instance().clearAll();

		sut = new Todo("Some Text");

		assertThat(EventHolder.instance().getLatestVersionOf(sut)).isEqualTo(0);
	}

	@Test
	void done() {
		assertThat(EventHolder.instance().getLatestVersionOf(sut)).isEqualTo(0);

		sut.done();

		assertThat(EventHolder.instance().getLatestVersionOf(sut)).isEqualTo(1);
	}


}

package io.agistep.todo.domain;

import io.agistep.event.EventList;
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
		TodoCreated created = TodoCreated.newBuilder().setText("Some Text").build();

		assertThatOccurredExactlyOnes(sut, created);
	}



	@Test
	void properties() {
		assertThat(sut.getText()).isEqualTo("Some Text");
		assertThat(sut.isDone()).isEqualTo(false);
	}
}
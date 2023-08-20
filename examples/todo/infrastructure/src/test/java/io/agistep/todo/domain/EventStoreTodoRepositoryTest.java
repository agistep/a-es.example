package io.agistep.todo.domain;

import io.agistep.event.EventStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.mockito.Mockito.verify;

@MockitoSettings
class EventStoreTodoRepositoryTest {

	@Mock
	Todo todo;

	@Mock
	EventStore eventStore;

	@InjectMocks
	EventStoreTodoRepository sut;

	@BeforeEach
	void setUp() {

	}

	@Test
	void save() {

		sut.save(todo);

		verify(eventStore).publishOccurredEventOf(todo);
	}

	@Test
	void findBy() {
		sut.findBy(1L);

		verify(eventStore).load(1L);
	}
}
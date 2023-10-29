package io.agistep.todo.domain;

import io.agistep.event.Event;
import io.agistep.event.storages.EventStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;

@MockitoSettings
class EventStoreTodoRepositoryTest {

	@Mock
	Todo todo;

	@Mock
	EventStorage eventStorage;


	@InjectMocks
	EventStoreTodoRepository sut;

	@Test
	void save() {
		List<Event> value = new ArrayList<>();

		sut.save(todo);

		verify(eventStorage).save(value);

	}

	@Test
	void findBy() {
		sut.findBy(1L);

		verify(eventStorage).findById(1L);
	}


}
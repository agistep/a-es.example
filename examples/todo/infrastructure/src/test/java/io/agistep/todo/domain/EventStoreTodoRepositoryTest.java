package io.agistep.todo.domain;

import io.agistep.event.Event;
import io.agistep.event.serialization.NoOpSerializer;
import io.agistep.event.storages.MapEventStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@MockitoSettings
class EventStoreTodoRepositoryTest {


	EventStoreTodoRepository sut;

	@BeforeEach
	void setUp() {
		Map<Long, List<Event>> map = new HashMap<>();
		sut = new EventStoreTodoRepository(new MapEventStorage(map, new NoOpSerializer()));
	}

	@Test
	void saveAndFindById() {

		Todo todo = new Todo("some text");

		sut.save(todo);

		Optional<Todo> find = sut.findById(todo.getId());

		assertThat(find).isPresent();
	}

}
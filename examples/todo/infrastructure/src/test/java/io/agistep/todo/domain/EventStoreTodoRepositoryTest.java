package io.agistep.todo.domain;

import io.agistep.event.Event;
import io.agistep.event.storages.MapEventStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@MockitoSettings
class EventStoreTodoRepositoryTest {


	EventStoreTodoRepository sut;

	@BeforeEach
	void setUp() {
		System.setProperty("basePackage", "io.agistep");
		Map<Long, List<Event>> map = new HashMap<>();
		sut = new EventStoreTodoRepository(new MapEventStorage(map));
	}

	@Test
	void saveAndFindById() {

		Todo todo = new Todo("some text");

		sut.save(todo);

		Optional<Todo> find = sut.findById(todo.getId());

		assertThat(find).isPresent();
	}

}

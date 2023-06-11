package io.agistep.todo.applications;

import io.agistep.event.EventStore;
import io.agistep.todo.domain.Todo;
import io.agistep.todo.domain.TodoFactory;
import io.agistep.todo.domain.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.mockito.Mockito.*;

@MockitoSettings
class TodoCreationApplicationServiceTest {

	@Spy
	TodoFactory todoFactory = new TodoFactory();
	ReturnCapture<Todo> createdTodo = new ReturnCapture<>();

	@Mock
	EventStore eventStore;

	@Mock
	TodoRepository todoRepository;

	TodoCreationApplicationService sut;

	@BeforeEach
	void setUp() {
		sut  = new TodoCreationApplicationService(todoFactory, todoRepository);
		doAnswer(createdTodo).when(todoFactory).create(anyString());
	}

	@Test
	void create() {
		sut.create("Some Todo");

		InOrder inOrder = inOrder(todoFactory, todoRepository);
		inOrder.verify(todoFactory).create("Some Todo");
		inOrder.verify(todoRepository).save(createdTodo.get());
	}
}
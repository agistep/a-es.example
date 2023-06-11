package io.agistep.todo.applications;

import io.agistep.event.EventList;
import io.agistep.event.EventStore;
import io.agistep.todo.domain.Todo;
import io.agistep.todo.domain.TodoFactory;
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
	ReturnCapture<Todo> returnCapture = new ReturnCapture<>();

	@Mock
	EventStore eventStore;

	TodoCreationApplicationService sut;

	@BeforeEach
	void setUp() {
		sut  = new TodoCreationApplicationService(todoFactory, eventStore);
		doAnswer(returnCapture).when(todoFactory).create(anyString());
	}

	@Test
	void create() {
		sut.create("Some Todo");

		InOrder inOrder = inOrder(todoFactory, eventStore);
		inOrder.verify(todoFactory).create("Some Todo");
		inOrder.verify(eventStore).publish(EventList.instance().occurredListBy(returnCapture.get()).get(0));
	}
}
package io.agistep.todo.applications;

import io.agistep.event.Event;
import io.agistep.event.EventStore;
import io.agistep.todo.domain.TodoFactory;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.mockito.Mockito.inOrder;

@MockitoSettings
class TodoCreationApplicationServiceTest {

	@Mock
	TodoFactory todoFactory;

	@Mock
	EventStore eventStore;

	@Mock
	Event expectedEvent;

	@Test
	void name() {

		TodoCreationApplicationService sut  = new TodoCreationApplicationService(todoFactory, eventStore);

		sut.create("Some Todo");

		InOrder inOrder = inOrder(todoFactory, eventStore);
//		inOrder.verify(todoFactory).create("Some Todo");
//		inOrder.verify(eventStore).appendToStream(expectedEvent);
	}
}
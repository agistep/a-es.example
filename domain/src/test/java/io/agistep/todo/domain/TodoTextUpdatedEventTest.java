package io.agistep.todo.domain;

import io.agistep.event.AggregateSupports;
import io.agistep.event.Events;
import io.agistep.identity.IdentityValueProvider;
import org.junit.jupiter.api.Test;

import static io.agistep.event.EventAssertions.assertThatOccurredExactlyOnes;

class TodoTextUpdatedEventTest {

	@Test
	void textChangedEvent() {
		Object payload = TodoCreated.newBuilder().setText("Some Text").build();
		long idValue = IdentityValueProvider.instance().newLong();
		Todo sut = Todo.reorganize(Events.events(idValue, payload));

		sut.updateText("Updated Text");


		final long aggregateIdValue = AggregateSupports.getId(sut);
		assertThatOccurredExactlyOnes(sut, Events.mock(aggregateIdValue, TodoTextUpdated.newBuilder().setUpdatedText("Updated Text").build()));
	}


}

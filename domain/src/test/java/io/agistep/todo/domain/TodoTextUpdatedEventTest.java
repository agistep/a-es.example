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
		Todo sut = Todo.replay(
				/*TODO 여기보자... 왜 begin 을 사용하는가? begin 을 사용하것을 왜 문제 시 삼으려 하는가?*/
				Events.begin(idValue, payload));

		sut.updateText("Updated Text");


		final long aggregateIdValue = AggregateSupports.getId(sut);
		assertThatOccurredExactlyOnes(sut, Events.occurs(aggregateIdValue, TodoTextUpdated.newBuilder().setUpdatedText("Updated Text").build()));
	}


}

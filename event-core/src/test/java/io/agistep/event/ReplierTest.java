package io.agistep.event;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReplierTest {


	@Test
	void replay() {

		Foo aggregate = new Foo();
		Event anEvent =Events.begin(1L, new FooCreated());

		assertThat(aggregate.id).isNull();

		EventReorganizer.reorganize(aggregate, anEvent);

		assertThat(aggregate.id.getValue()).isEqualTo(1L);
	}
}
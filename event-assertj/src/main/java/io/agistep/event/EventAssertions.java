package io.agistep.event;


import org.assertj.core.api.Assertions;

public final class EventAssertions {

	public static void assertThatOccurredExactly(Object aggregate, Event... event) {
		Assertions.assertThat(EventList.instance().occurredListBy(aggregate)).containsExactly(event);
	}

	public static void assertThatDoesNotOccurAnEventBy(Object aggregate) {
		Assertions.assertThat(EventList.instance().occurredListBy(aggregate)).isEmpty();
	}
}

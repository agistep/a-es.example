package io.agistep.event;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class EventBuilderTest {

	final long aggregateIdValue = 0L;
	final SomePayload payload = new SomePayload();


	@Test
	void beginEvent() {
		Map<Long, Long> ids = new HashMap<>();

		Event actual = Events.create(aggregateIdValue, payload, ids);

		assertThat(actual.getName()).isEqualTo(SomePayload.class.getName());
		assertThat(actual.getOrder()).isEqualTo(Events.BEGIN_ORDER);
		assertThat(actual.getAggregateIdValue()).isEqualTo(aggregateIdValue);
		assertThat(actual.getPayload()).isSameAs(payload);
		assertThat(actual.getOccurredAt()).isEqualToIgnoringSeconds(LocalDateTime.now());
	}


	@Test
	void occurs() {
		Event actual = Events.builder()
				.name(((Object) payload).getClass().getName())
				.order(-1) //TODO 이전 order 를 알아야한다.
				.aggregateIdValue(aggregateIdValue)
				.payload(payload)
				.occurredAt(LocalDateTime.now())
				.build();

		assertThat(actual.getName()).isEqualTo(SomePayload.class.getName());
		assertThat(actual.getOrder()).isNotEqualTo(Events.BEGIN_ORDER);
		assertThat(actual.getAggregateIdValue()).isEqualTo(aggregateIdValue);
		assertThat(actual.getPayload()).isSameAs(payload);
		assertThat(actual.getOccurredAt()).isEqualToIgnoringSeconds(LocalDateTime.now());

	}
}
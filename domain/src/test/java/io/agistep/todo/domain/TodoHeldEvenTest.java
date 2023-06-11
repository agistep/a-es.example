package io.agistep.todo.domain;

import io.agistep.event.Event;
import io.agistep.event.Events;
import org.junit.jupiter.api.Test;

import static io.agistep.event.EventAssertions.assertThatDoesNotOccurAnEventBy;
import static org.assertj.core.api.Assertions.assertThat;

class TodoHeldEvenTest {


	@Test
	void hold() {
		//TODO 왜 아래는(created) 인라인으로 들어갈 수 가 없는가.?
		//TODO 1을 aggreagteIDValue 로 넣으면 어떤일이 발생하는가?
		//TODO order 는 어떤 이유로 1,2 를 넣은것인가?
		//TODO 조금더 타당하고 명시적인 어그리거트 구체화 를 테스트에서 생성할수 없을까?
		Event created = Events.mock(/*1*/99, 1, TodoCreated.newBuilder().setText("Some Text").build());
		Todo sut = Todo.replay(
				created,
				Events.mock(created.getAggregateIdValue(), 2, TodoDone.newBuilder().build()));
		assertThat(sut.isDone()).isTrue();

		sut.hold();

		assertThatDoesNotOccurAnEventBy(sut);
	}
}

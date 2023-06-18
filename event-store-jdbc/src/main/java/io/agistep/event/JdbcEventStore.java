package io.agistep.event;

import org.valid4j.Assertive;

import javax.sql.DataSource;
import java.util.List;

import static org.valid4j.Assertive.require;

class JdbcEventStore implements EventStore {

	JdbcEventStore(DataSource dataSource) {
		require(dataSource !=null);
	}

	@Override
	public void publishOccurredEventOf(Object aggregate) {

	}

	@Override
	public List<Event> load(long todoId) {
		return EventStore.super.load(todoId);
	}
}

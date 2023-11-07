package io.agistep.event;

import java.util.HashMap;
import java.util.Map;

class ThreadLocalEventVersionHolder {

	private final static ThreadLocal<Map<Object,Long>> changes = ThreadLocal.withInitial(HashMap::new);

	public static ThreadLocalEventVersionHolder instance() {
		return new ThreadLocalEventVersionHolder();
	}

	private ThreadLocalEventVersionHolder() {
		// Do Nothing
	}

	public void setVersion(long aggregateId, long version) {
		changes.get().put(aggregateId, version);
	}



	public void clearAll() {
		changes.remove();
	}

	public void clear(Object aggregate) {
		//TODO
		changes.remove();
	}

	long nextVersion(Long aggregateId) {
		return getVersion(aggregateId)+1;
	}

	long getVersion(Long aggregateId) {
		return changes.get().getOrDefault(aggregateId, -1L);
	}
}
